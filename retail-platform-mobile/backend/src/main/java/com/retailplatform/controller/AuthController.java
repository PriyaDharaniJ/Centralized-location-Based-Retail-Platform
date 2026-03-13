package com.retailplatform.controller;
import com.retailplatform.dto.*;
import com.retailplatform.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    @Autowired private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest req) {
        try {
            return ResponseEntity.ok(ApiResponse.ok(authService.login(req)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/register/user")
    public ResponseEntity<?> registerUser(@RequestBody UserRegisterRequest req) {
        try {
            return ResponseEntity.ok(ApiResponse.ok(authService.registerUser(req)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/register/retailer")
    public ResponseEntity<?> registerRetailer(@RequestBody RetailerRegisterRequest req) {
        try {
            return ResponseEntity.ok(ApiResponse.ok(authService.registerRetailer(req)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}
