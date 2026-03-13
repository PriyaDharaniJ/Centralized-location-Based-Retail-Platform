package com.retailplatform.controller;
import com.retailplatform.dto.ApiResponse;
import com.retailplatform.service.NotificationService;
import com.retailplatform.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "*")
public class NotificationController {
    @Autowired private NotificationService notifService;
    @Autowired private JwtUtil jwtUtil;
    private Long getId(String auth) { return jwtUtil.extractId(auth.substring(7)); }
    private String getRole(String auth) { return jwtUtil.extractRole(auth.substring(7)); }

    @GetMapping
    public ResponseEntity<?> getNotifications(@RequestHeader("Authorization") String auth) {
        Long id = getId(auth); String role = getRole(auth);
        return ResponseEntity.ok(ApiResponse.ok(notifService.getNotifications(role, id)));
    }

    @GetMapping("/count")
    public ResponseEntity<?> getUnreadCount(@RequestHeader("Authorization") String auth) {
        Long id = getId(auth); String role = getRole(auth);
        return ResponseEntity.ok(ApiResponse.ok(notifService.getUnreadCount(role, id)));
    }

    @PostMapping("/read-all")
    public ResponseEntity<?> markAllRead(@RequestHeader("Authorization") String auth) {
        Long id = getId(auth); String role = getRole(auth);
        notifService.markAllRead(role, id);
        return ResponseEntity.ok(ApiResponse.ok("All notifications marked as read", null));
    }
}
