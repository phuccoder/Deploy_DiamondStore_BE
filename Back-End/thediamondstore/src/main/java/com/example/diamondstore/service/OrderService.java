package com.example.diamondstore.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.diamondstore.DTO.OrderSummaryDTO;
import com.example.diamondstore.model.Account;
import com.example.diamondstore.model.AccumulatePoints;
import com.example.diamondstore.model.Cart;
import com.example.diamondstore.model.Diamond;
import com.example.diamondstore.model.Jewelry;
import com.example.diamondstore.model.Order;
import com.example.diamondstore.model.Promotion;
import com.example.diamondstore.repository.AccountRepository;
import com.example.diamondstore.repository.AccumulatePointsRepository;
import com.example.diamondstore.repository.CartRepository;
import com.example.diamondstore.repository.CertificateRepository;
import com.example.diamondstore.repository.DiamondRepository;
import com.example.diamondstore.repository.JewelryRepository;
import com.example.diamondstore.repository.OrderRepository;
import com.example.diamondstore.repository.PromotionRepository;
import com.example.diamondstore.repository.WarrantyRepository;
import com.example.diamondstore.request.putRequest.OrderPutRequest;

@Service
public class OrderService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PromotionRepository promotionRepository;

    @Autowired
    private AccumulatePointsRepository accumulatePointsRepository;

    @Autowired
    private WarrantyRepository warrantyRepository;

    @Autowired
    private CertificateRepository certificateRepository;

    @Autowired
    private DiamondRepository diamondRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private JewelryRepository jewelryRepository;

    @Transactional
    public Order createOrder(int accountID, String deliveryAddress, String promotionCode, Integer pointsToRedeem,
            String phoneNumber) {
        List<Cart> cartItems = cartRepository.findByAccount_AccountID(accountID);

        if (cartItems.isEmpty()) {
            throw new IllegalArgumentException("Giỏ hàng rỗng");
        }

        Account account = accountRepository.findById(accountID)
                .orElseThrow(() -> new IllegalArgumentException("Invalid account ID: " + accountID));

        Order order = new Order();
        order.setAccount(account);
        order.setDeliveryAddress(deliveryAddress);
        order.setPhoneNumber(phoneNumber);
        order.setAccountPoint(pointsToRedeem);
        order.setStartorderDate(LocalDateTime.now());
        order.setDeliveryDate(LocalDateTime.now().plusDays(7));
        order.setOrderStatus("Đang xử lý");

        accountRepository.save(account);

        // Lưu Order trước
        order = orderRepository.save(order);

        BigDecimal totalOrder = BigDecimal.ZERO;
        for (Cart cart : cartItems) {
            Diamond diamond = cart.getDiamond();
            if (diamond != null) {
                if (cart.getQuantity() > diamond.getQuantity()) {
                    throw new IllegalArgumentException("Số lượng kim cương không đủ");
                }
                diamond.setQuantity(diamond.getQuantity() - cart.getQuantity());
                diamondRepository.save(diamond);
            }

            Jewelry jewelry = cart.getJewelry();
            if (jewelry != null) {
                if (cart.getQuantity() > jewelry.getQuantity()) {
                    throw new IllegalArgumentException("Số lượng trang sức không đủ");
                }
                jewelry.setQuantity(jewelry.getQuantity() - cart.getQuantity());
                jewelryRepository.save(jewelry);
            }

            totalOrder = totalOrder.add(cart.getGrossCartPrice());
            cart.setOrder(order);
            cart.setCartStatus("Đang chờ thanh toán");
            cartRepository.save(cart);
        }

        Promotion promotion = promotionRepository.findByPromotionCode(promotionCode);
        if (promotion != null) {
            BigDecimal discountAmount = promotion.getDiscountAmount();
            totalOrder = totalOrder.subtract(totalOrder.multiply(discountAmount));
            order.setPromotionCode(promotionCode);
        }

        if (pointsToRedeem != null && pointsToRedeem > 0) {
            AccumulatePoints accumulatePoints = accumulatePointsRepository.findById(accountID)
                    .orElseThrow(() -> new IllegalArgumentException("Khách hàng không tồn tại"));
            int availablePoints = accumulatePoints.getPoint();
            if (pointsToRedeem > availablePoints) {
                throw new IllegalArgumentException("Điểm không đủ");
            }
            BigDecimal discount = BigDecimal.valueOf(pointsToRedeem / 100.0);
            totalOrder = totalOrder.subtract(discount.multiply(BigDecimal.valueOf(1000000)));
            accumulatePoints.setPoint(availablePoints - pointsToRedeem);
            accumulatePointsRepository.save(accumulatePoints);
        }

        order.setTotalOrder(totalOrder);

        // Lưu Order một lần nữa để cập nhật totalOrder
        order = orderRepository.save(order);

        scheduleOrderStatusCheck(order);

        return order;
    }

    private void scheduleOrderStatusCheck(Order order) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                handleOrderTimeout(order);
            }
        }, 150000); // 2.5 minutes
    }

    @Transactional
    private void handleOrderTimeout(Order order) {
        // Reload the order from the database to get the latest status
        Order currentOrder = orderRepository.findById(order.getOrderID()).orElse(null);
        if (currentOrder != null && currentOrder.getOrderStatus().equals("Đang xử lý")) {
            currentOrder.setOrderStatus("Thất bại");
            orderRepository.save(currentOrder);

            // Hoàn trả số lượng sản phẩm về hệ thống
            for (Cart cart : currentOrder.getCartItems()) {
                if (cart.getDiamond() != null) {
                    Diamond diamond = cart.getDiamond();
                    diamond.setQuantity(diamond.getQuantity() + cart.getQuantity());
                    diamondRepository.save(diamond);
                } else if (cart.getJewelry() != null) {
                    Jewelry jewelry = cart.getJewelry();
                    jewelry.setQuantity(jewelry.getQuantity() + cart.getQuantity());
                    jewelryRepository.save(jewelry);
                }
            }
        }
    }

    public void cancelOrder(int orderID) {
        Order order = orderRepository.findById(orderID)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        if (!order.getOrderStatus().equals("Đang xử lý") && !order.getOrderStatus().equals("Thất bại")) {
            throw new IllegalStateException("Chỉ Order có Status 'Đang xử lý' mới được xóa");
        }

        // Delete the Cart items associated with the Order
        List<Cart> carts = order.getCartItems();
        for (Cart cart : carts) {
            cartRepository.delete(cart);
        }

        // Now delete the Order
        orderRepository.delete(order);
    }

    public Order getOrder(int orderID) {
        Order order = orderRepository.findByOrderID(orderID);
        order.getCartItems().size(); // This will fetch the cartItems from the database
        return order;
    }

    public List<Order> getOrdersByAccountId(int accountID) {
        Account account = accountRepository.findById(accountID)
                .orElseThrow(() -> new IllegalArgumentException("AccountID không tồn tại"));
        List<Order> orders = orderRepository.findByAccount(account);
        if (orders.isEmpty()) {
            throw new IllegalArgumentException("Chưa có Order nào");
        }
        return orders;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll(Sort.by(Sort.Direction.DESC, "startorderDate"));
    }

    public Object getTotalOrder(Integer orderID) {
        Order order = orderRepository.findByOrderID(orderID);
        if (order == null) {
            throw new IllegalArgumentException("Không tìm thấy Order");
        }
        return order.getTotalOrder();
    }

    @Transactional
    public ResponseEntity<?> updateOrder(int orderID, OrderPutRequest orderPutRequest) {
        try {
            Order existingOrder = orderRepository.findById(orderID)
                    .orElseThrow(() -> new IllegalArgumentException("Order not found"));

            LocalDateTime deliveryDate = orderPutRequest.getDeliveryDate();
            LocalDateTime today = LocalDateTime.now();

            existingOrder.setDeliveryAddress(orderPutRequest.getDeliveryAddress());
            existingOrder.setOrderStatus(orderPutRequest.getOrderStatus());

            if (deliveryDate != null && deliveryDate.isAfter(today)) {
                existingOrder.setDeliveryDate(deliveryDate);
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ngày giao hàng không hợp lệ");
            }

            // Handle promotion code and totalOrder update
            String newPromotionCode = orderPutRequest.getPromotionCode();
            BigDecimal totalOrder = existingOrder.getTotalOrder();

            if (newPromotionCode != null && !newPromotionCode.isEmpty()) {
                Promotion promotion = promotionRepository.findByPromotionCode(newPromotionCode);
                if (promotion != null) {
                    BigDecimal discountAmount = promotion.getDiscountAmount();
                    totalOrder = totalOrder.subtract(totalOrder.multiply(discountAmount));
                    existingOrder.setPromotionCode(newPromotionCode);
                }
            }

            existingOrder.setTotalOrder(totalOrder);
            orderRepository.save(existingOrder);
            return ResponseEntity.ok(Collections.singletonMap("message", "Cập nhật thành công"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Cập nhật thất bại"));
        }
    }

    public List<Order> getOrdersByStatus(String orderStatus) {
        return orderRepository.findByOrderStatus(orderStatus);
    }

    public Page<Order> getAllOrdersPaged(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return orderRepository.findAll(pageable);
    }

    public List<Order> getOrdersHaveTransactionNo() {
        return orderRepository.findByTransactionNoNotNull();
    }

    public BigDecimal getTotalOrderByOrderStatusPaid() {
        List<Order> paidOrders = orderRepository.findAllByOrderStatus("Đã thanh toán");
        BigDecimal total = BigDecimal.ZERO;
        for (Order order : paidOrders) {
            total = total.add(order.getTotalOrder());
        }
        return total;
    }

    public Long getTotalOrdersByOrderStatus(String orderStatus) {
        return orderRepository.countByOrderStatus("Đã thanh toán");
    }

    public Long getTotalOrders() {
        return orderRepository.count();
    }

    // Lấy tổng số Order trong 1 ngày
    public int getTotalOrdersInDay() {
        LocalDateTime start = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime end = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59).withNano(999999999);
        return orderRepository.countByStartorderDateBetween(start, end);
    }

    // tính tổng totalOrder của tất cả Order trong 1 ngày
    public OrderSummaryDTO getRevenueValueInToday() {
        LocalDateTime start = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime end = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59).withNano(999999999);
        List<Order> orders = orderRepository.findAllByStartorderDateBetween(start, end);
        BigDecimal total = BigDecimal.ZERO;
        for (Order order : orders) {
            total = total.add(order.getTotalOrder());
        }

        // tạo một đối tượng OrderSummary để lưu trữ kết quả
        OrderSummaryDTO summary = new OrderSummaryDTO(LocalDate.now(), total);
        return summary;
    }

    // tính tổng totalOrder của tất cả Order từng ngày trong 1 tháng
    public List<OrderSummaryDTO> getRevenueDayInMonth() {
        LocalDate now = LocalDate.now();
        int month = now.getMonthValue();
        int year = now.getYear();
        int dayOfMonth = now.lengthOfMonth();
        List<OrderSummaryDTO> summaries = new ArrayList<>();
        for (int i = 1; i <= dayOfMonth; i++) {
            LocalDate date = LocalDate.of(year, month, i);
            LocalDateTime start = date.atStartOfDay();
            LocalDateTime end = date.atTime(23, 59, 59, 999999999);
            List<Order> orders = orderRepository.findAllByStartorderDateBetween(start, end);
            BigDecimal total = BigDecimal.ZERO;
            for (Order order : orders) {
                total = total.add(order.getTotalOrder());
            }
            OrderSummaryDTO summary = new OrderSummaryDTO(date, total);
            summaries.add(summary);
        }
        return summaries;
    }

    // tính tổng totalOrder của tất cả Order từng ngày trong tháng (đã chọn) trong
    // năm (đã chọn)
    public List<OrderSummaryDTO> getRevenueDayInMonthInYear(int month, int year) {
        int dayOfMonth = LocalDate.of(year, month, 1).lengthOfMonth();
        List<OrderSummaryDTO> summaries = new ArrayList<>();
        for (int i = 1; i <= dayOfMonth; i++) {
            LocalDate date = LocalDate.of(year, month, i);
            LocalDateTime start = date.atStartOfDay();
            LocalDateTime end = date.atTime(23, 59, 59, 999999999);
            List<Order> orders = orderRepository.findAllByStartorderDateBetween(start, end);
            BigDecimal revenue = BigDecimal.ZERO;
            for (Order order : orders) {
                revenue = revenue.add(order.getTotalOrder());
            }
            OrderSummaryDTO summary = new OrderSummaryDTO(date, revenue);
            summaries.add(summary);
        }
        return summaries;
    }

    // tính tổng totalOrder của tất cả Order từng tháng trong 1 năm
    public List<OrderSummaryDTO> getRevenueMonthInYear() {
        LocalDate now = LocalDate.now();
        int year = now.getYear();
        List<OrderSummaryDTO> summaries = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            LocalDate startOfMonth = LocalDate.of(year, i, 1);
            LocalDate endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());
            LocalDateTime start = startOfMonth.atStartOfDay();
            LocalDateTime end = endOfMonth.atTime(23, 59, 59, 999999999);
            List<Order> orders = orderRepository.findAllByStartorderDateBetween(start, end);
            BigDecimal revenue = BigDecimal.ZERO;
            for (Order order : orders) {
                revenue = revenue.add(order.getTotalOrder());
            }
            OrderSummaryDTO summary = new OrderSummaryDTO(endOfMonth, revenue);
            summaries.add(summary);
        }
        return summaries;
    }

    // tính tổng total Order của tất cả Order từng tháng trong năm (đã chọn)
    public List<OrderSummaryDTO> getRevenueMonthInYear(int year) {
        List<OrderSummaryDTO> summaries = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            LocalDate startOfMonth = LocalDate.of(year, i, 1);
            LocalDate endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());
            LocalDateTime start = startOfMonth.atStartOfDay();
            LocalDateTime end = endOfMonth.atTime(23, 59, 59, 999999999);
            List<Order> orders = orderRepository.findAllByStartorderDateBetween(start, end);
            BigDecimal revenue = BigDecimal.ZERO;
            for (Order order : orders) {
                revenue = revenue.add(order.getTotalOrder());
            }
            OrderSummaryDTO summary = new OrderSummaryDTO(endOfMonth, revenue);
            summaries.add(summary);
        }
        return summaries;
    }

    // last in first out order with accountID
    public Integer LIFO(Integer accountID) {
        Optional<Account> accountOpt = accountRepository.findById(accountID);

        if (accountOpt.isPresent()) {
            Account account = accountOpt.get();
            List<Order> orders = orderRepository.findByAccount(account);

            if (orders.isEmpty()) {
                return null; // or throw an exception if preferred
            }

            // Sort orders by creation date or ID in descending order
            orders.sort((o1, o2) -> o2.getStartorderDate().compareTo(o1.getStartorderDate())); // or
                                                                                               // o2.getId().compareTo(o1.getId())

            // Get the first order ID from the sorted list
            Integer max = orders.get(0).getOrderID();

            return max;
        } else {
            return null;
        }
    }
}
