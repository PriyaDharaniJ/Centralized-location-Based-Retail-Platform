package com.retailplatform.controller;
import com.retailplatform.dto.ApiResponse;
import com.retailplatform.service.CartService;
import com.retailplatform.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/user/cart")
@CrossOrigin(origins = "*")
public class CartController {
    @Autowired private CartService cartService;
    @Autowired private JwtUtil jwtUtil;
    private Long getId(String auth) { return jwtUtil.extractId(auth.substring(7)); }

    @GetMapping
    public ResponseEntity<?> getCart(@RequestHeader("Authorization") String auth) {
        return ResponseEntity.ok(ApiResponse.ok(cartService.getCart(getId(auth))));
    }

    @PostMapping
    public ResponseEntity<?> addToCart(@RequestHeader("Authorization") String auth,
                                        @RequestBody Map<String, Object> body) {
        try {
            Long productId = Long.valueOf(body.get("productId").toString());
            Integer qty = Integer.valueOf(body.get("quantity").toString());
            return ResponseEntity.ok(ApiResponse.ok(cartService.addToCart(getId(auth), productId, qty)));
        } catch (Exception e) { return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage())); }
    }

    @PutMapping("/{productId}")
    public ResponseEntity<?> updateCart(@RequestHeader("Authorization") String auth,
                                         @PathVariable Long productId, @RequestBody Map<String, Integer> body) {
        return ResponseEntity.ok(ApiResponse.ok(cartService.updateCart(getId(auth), productId, body.get("quantity"))));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<?> removeFromCart(@RequestHeader("Authorization") String auth, @PathVariable Long productId) {
        cartService.removeFromCart(getId(auth), productId);
        return ResponseEntity.ok(ApiResponse.ok("Removed", null));
    }

    @DeleteMapping
    public ResponseEntity<?> clearCart(@RequestHeader("Authorization") String auth) {
        cartService.clearCart(getId(auth));
        return ResponseEntity.ok(ApiResponse.ok("Cart cleared", null));
    }
}
