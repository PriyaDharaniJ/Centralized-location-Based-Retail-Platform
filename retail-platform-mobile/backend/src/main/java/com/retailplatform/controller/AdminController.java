package com.retailplatform.controller;
import com.retailplatform.dto.ApiResponse;
import com.retailplatform.service.AdminService;
import com.retailplatform.service.NotificationService;
import com.retailplatform.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {
    @Autowired private AdminService adminService;
    @Autowired private NotificationService notifService;
    @Autowired private JwtUtil jwtUtil;

    @GetMapping("/dashboard")
    public ResponseEntity<?> getDashboard() {
        return ResponseEntity.ok(ApiResponse.ok(adminService.getDashboardStats()));
    }

    @GetMapping("/retailers")
    public ResponseEntity<?> getAllRetailers() {
        return ResponseEntity.ok(ApiResponse.ok(adminService.getAllRetailers()));
    }

    @GetMapping("/retailers/pending")
    public ResponseEntity<?> getPendingRetailers() {
        return ResponseEntity.ok(ApiResponse.ok(adminService.getPendingRetailers()));
    }

    @PostMapping("/retailers/{id}/approve")
    public ResponseEntity<?> approveRetailer(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(ApiResponse.ok(adminService.approveRetailer(id)));
        } catch (Exception e) { return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage())); }
    }

    @PostMapping("/retailers/{id}/reject")
    public ResponseEntity<?> rejectRetailer(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(ApiResponse.ok(adminService.rejectRetailer(id)));
        } catch (Exception e) { return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage())); }
    }
}
