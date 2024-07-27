package com.example.diamondstore.controller.Cart;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.diamondstore.model.Cart;
import com.example.diamondstore.service.CartService;

@RestController
@RequestMapping("/api/customer/carts")
public class CartController {

    @Autowired
    private CartService cartService;

     @GetMapping("/{accountID}")
    public ResponseEntity<List<Cart>> getCartItems(@PathVariable Integer accountID) {
        List<Cart> cartItems = cartService.getCartItems(accountID);
        if (cartItems.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(cartItems, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Map<String, String>> addItemToCart(
            @RequestParam(required = false) Integer accountID,
            @RequestParam(required = false) String diamondID,
            @RequestParam(required = false) String jewelryID,
            @RequestParam(required = false) Integer sizeJewelry,
            @RequestParam Integer quantity) {
        cartService.addItemToCart(accountID, diamondID, jewelryID, sizeJewelry, quantity);
        // Check jewelryID and sizeJewelry
        if (sizeJewelry == null && jewelryID != null) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Vui lòng nhập size"));
        }
        return ResponseEntity.ok(Collections.singletonMap("message", "Thêm vào giỏ hàng thành công"));
    }

    @PutMapping("/update/{cartID}")
    public ResponseEntity<Map<String, String>> updateCartItem(
            @PathVariable Integer cartID,
            @RequestParam Integer accountID,
            @RequestParam(required = false) String diamondID,
            @RequestParam(required = false) String jewelryID,
            @RequestParam(required = false) Integer sizeJewelry,
            @RequestParam Integer quantity) {
        cartService.updateCartItem(cartID, accountID, diamondID, jewelryID, sizeJewelry, quantity);
        return ResponseEntity.ok(Collections.singletonMap("message", "Cập nhật giỏ hàng thành công"));
    }

    @DeleteMapping(value = "/delete/{cartID}")
    public ResponseEntity<Map<String, String>> removeCartItem(@PathVariable Integer cartID) {
        cartService.removeCartItem(cartID);
        return ResponseEntity.ok(Collections.singletonMap("message", "Xóa khỏi giỏ hàng thành công"));
    }

    @GetMapping(value = "/total-cart")
    public ResponseEntity<?> getTotalCart(@RequestParam Integer accountID) {
        BigDecimal totalCart = cartService.getTotalCart(accountID);
        return ResponseEntity.ok(totalCart);
    }
}
