package com.example.diamondstore.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.diamondstore.model.Cart;
import com.example.diamondstore.model.Diamond;
import com.example.diamondstore.model.Jewelry;
import com.example.diamondstore.repository.CartRepository;
import com.example.diamondstore.repository.DiamondRepository;
import com.example.diamondstore.repository.JewelryRepository;
import com.example.diamondstore.repository.AccountRepository;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private DiamondRepository diamondRepository;

    @Autowired
    private JewelryRepository jewelryRepository;

    @Autowired
    private AccountRepository accountRepository;

    public List<Cart> getCartItems(Integer accountID) {
        return cartRepository.findByAccount_AccountID(accountID);
    }

    @Transactional
    public ResponseEntity<?> addItemToCart(Integer accountID, String diamondID, String jewelryID, Integer sizeJewelry, Integer quantity) {
        if (quantity <= 0) {
            return ResponseEntity.badRequest().body("Số lượng phải lớn hơn 0");
        }

        Cart cart = new Cart();
        cart.setAccount(accountRepository.findById(accountID).orElse(null));

        if (diamondID != null) {
            Diamond diamond = diamondRepository.findById(diamondID).orElse(null);
            if (diamond != null) {
                if (quantity > diamond.getQuantity()) {
                    return ResponseEntity.badRequest().body("Số lượng kim cương không đủ");
                }
                cart.setDiamond(diamond);
            }
        }

        if (jewelryID != null) {
            Jewelry jewelry = jewelryRepository.findById(jewelryID).orElse(null);
            if (jewelry != null) {
                if (quantity > jewelry.getQuantity()) {
                    return ResponseEntity.badRequest().body("Số lượng trang sức không đủ");
                }
                cart.setJewelry(jewelry);
                cart.setSizeJewelry(sizeJewelry);
            }
        }

        cart.setQuantity(quantity);
        calculateAndSetTotalPrice(cart);
        cartRepository.save(cart);
        return ResponseEntity.ok("Sản phẩm đã được thêm vào giỏ hàng.");
    }

    @Transactional
    public ResponseEntity<?> updateCartItem(Integer cartID, Integer accountID, String diamondID, String jewelryID,
            Integer sizeJewelry, Integer quantity) {
        Cart cartItem = cartRepository.findById(cartID).orElse(null);
        if (cartItem != null) {
            cartItem.setAccount(accountRepository.findById(accountID).orElse(null));

            if (quantity <= 0) {
                return ResponseEntity.badRequest().body("Số lượng phải lớn hơn 0");
            }

            if (diamondID != null) {
                Diamond diamond = diamondRepository.findById(diamondID).orElse(null);
                if (diamond != null) {
                    if (quantity > diamond.getQuantity()) {
                        return ResponseEntity.badRequest().body("Số lượng kim cương không đủ");
                    }
                    cartItem.setDiamond(diamond);
                } else {
                    return ResponseEntity.badRequest().body("Kim cương không tồn tại");
                }
            }

            if (jewelryID != null) {
                Jewelry jewelry = jewelryRepository.findById(jewelryID).orElse(null);
                if (jewelry != null) {
                    cartItem.setJewelry(jewelry);
                    cartItem.setSizeJewelry(sizeJewelry);
                } else {
                    return ResponseEntity.badRequest().body("Trang sức không tồn tại");
                }
            }

            cartItem.setQuantity(quantity);
            calculateAndSetTotalPrice(cartItem);
            cartRepository.save(cartItem);
            return ResponseEntity.ok(cartItem);
        } else {
            return ResponseEntity.badRequest().body("Giỏ hàng không tồn tại");
        }
    }

    @Transactional
    public void removeCartItem(Integer cartID) {
        Cart cart = cartRepository.findById(cartID).orElse(null);
        if (cart != null) {
            cartRepository.delete(cart);
        } else {
            throw new IllegalArgumentException("Không tìm thấy sản phẩm trong giỏ hàng.");
        }
    }

    private void calculateAndSetTotalPrice(Cart cart) {
        BigDecimal totalPrice = BigDecimal.ZERO;
        BigDecimal diamondPrice = BigDecimal.ZERO;
        BigDecimal jewelryPrice = BigDecimal.ZERO;

        if (cart.getDiamond() != null) {
            diamondPrice = diamondPrice.add(cart.getDiamond().getDiamondEntryPrice());
        }

        if (cart.getJewelry() != null) {
            jewelryPrice = jewelryPrice.add(cart.getJewelry().getJewelryEntryPrice());
        }

        BigDecimal price = diamondPrice.add(jewelryPrice);
        cart.setPrice(price);

        totalPrice = price.multiply(BigDecimal.valueOf(cart.getQuantity()));

        BigDecimal grossCartPrice;
        if (cart.getDiamond() != null && cart.getJewelry() != null) {
            grossCartPrice = totalPrice.multiply(BigDecimal.valueOf(1.2));
        } else {
            grossCartPrice = totalPrice.multiply(BigDecimal.valueOf(1.1));
        }
        cart.setGrossCartPrice(grossCartPrice);
    }

    public Cart getCartByCartID(Integer cartID) {
        return cartRepository.findById(cartID).orElse(null);
    }

    @Transactional
    public void saveCart(Cart cart) {
        calculateAndSetTotalPrice(cart);
        cartRepository.save(cart);
    }

    public BigDecimal getTotalCart(Integer accountID) {
        List<Cart> cartItems = cartRepository.findByAccount_AccountID(accountID);
        BigDecimal totalCart = BigDecimal.ZERO;
        for (Cart cart : cartItems) {
            totalCart = totalCart.add(cart.getGrossCartPrice());
        }
        return totalCart;
    }

    public String updateCartQuantity(Integer cartID, Integer quantity) {
        Cart cart = cartRepository.findById(cartID).orElse(null);
        if (cart != null) {
            cart.setQuantity(quantity);
            calculateAndSetTotalPrice(cart);
            cartRepository.save(cart);
            return "Số lượng sản phẩm đã được cập nhật.";
        } else {
            return "Không tìm thấy sản phẩm trong giỏ hàng.";
        }
    }
}
