package com.retailplatform.controller;
import com.retailplatform.dto.ApiResponse;
import com.retailplatform.service.WishlistService;
import com.retailplatform.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/user/wishlist")
@CrossOrigin(origins = "*")
public class WishlistController {
    @Autowired private WishlistService wishlistService;
    @Autowired private JwtUtil jwtUtil;
    private Long getId(String auth) { return jwtUtil.extractId(auth.substring(7)); }

    @GetMapping
    public ResponseEntity<?> getWishlist(@RequestHeader("Authorization") String auth) {
        return ResponseEntity.ok(ApiResponse.ok(wishlistService.getWishlist(getId(auth))));
    }

    @PostMapping("/toggle/{productId}")
    public ResponseEntity<?> toggleWishlist(@RequestHeader("Authorization") String auth, @PathVariable Long productId) {
        boolean added = wishlistService.toggleWishlist(getId(auth), productId);
        return ResponseEntity.ok(ApiResponse.ok(added ? "Added to wishlist" : "Removed from wishlist", added));
    }
}
