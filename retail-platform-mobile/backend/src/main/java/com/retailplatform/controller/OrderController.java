package com.retailplatform.controller;
import com.retailplatform.dto.ApiResponse;
import com.retailplatform.dto.OrderRequest;
import com.retailplatform.service.OrderService;
import com.retailplatform.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class OrderController {
    @Autowired private OrderService orderService;
    @Autowired private JwtUtil jwtUtil;

    private Long getIdFromToken(String auth) { return jwtUtil.extractId(auth.substring(7)); }

    @PostMapping("/user/orders")
    public ResponseEntity<?> placeOrder(@RequestHeader("Authorization") String auth,
                                         @RequestBody OrderRequest req) {
        try {
            Long userId = getIdFromToken(auth);
            return ResponseEntity.ok(ApiResponse.ok(orderService.placeOrder(userId, req)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/user/orders")
    public ResponseEntity<?> getUserOrders(@RequestHeader("Authorization") String auth) {
        Long userId = getIdFromToken(auth);
        return ResponseEntity.ok(ApiResponse.ok(orderService.getUserOrders(userId)));
    }

    @GetMapping("/track/{trackingId}")
    public ResponseEntity<?> trackOrder(@PathVariable String trackingId) {
        try {
            return ResponseEntity.ok(ApiResponse.ok(orderService.trackOrder(trackingId)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/retailer/orders")
    public ResponseEntity<?> getRetailerOrders(@RequestHeader("Authorization") String auth) {
        Long id = getIdFromToken(auth);
        return ResponseEntity.ok(ApiResponse.ok(orderService.getRetailerOrders(id)));
    }

    @PatchMapping("/retailer/orders/{orderId}/status")
    public ResponseEntity<?> updateOrderStatus(@RequestHeader("Authorization") String auth,
                                                @PathVariable Long orderId,
                                                @RequestBody Map<String, String> body) {
        try {
            Long id = getIdFromToken(auth);
            return ResponseEntity.ok(ApiResponse.ok(orderService.updateOrderStatus(orderId, id, body.get("status"))));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}
