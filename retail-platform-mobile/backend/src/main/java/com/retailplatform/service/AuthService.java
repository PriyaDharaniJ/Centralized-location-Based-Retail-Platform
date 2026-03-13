package com.retailplatform.service;
import com.retailplatform.dto.*;
import com.retailplatform.entity.*;
import com.retailplatform.repository.*;
import com.retailplatform.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
public class AuthService {
    @Autowired private UserRepository userRepo;
    @Autowired private RetailOwnerRepository retailerRepo;
    @Autowired private AdminRepository adminRepo;
    @Autowired private PasswordEncoder encoder;
    @Autowired private JwtUtil jwtUtil;

    public AuthResponse login(AuthRequest req) {
        String role = req.getRole() != null ? req.getRole().toUpperCase() : "USER";
        switch (role) {
            case "ADMIN" -> {
                Admin admin = adminRepo.findByEmail(req.getEmail())
                        .orElseThrow(() -> new RuntimeException("Invalid credentials"));
                if (!encoder.matches(req.getPassword(), admin.getPassword()))
                    throw new RuntimeException("Invalid credentials");
                return new AuthResponse(jwtUtil.generateToken(admin.getEmail(), "ADMIN", admin.getId()),
                        "ADMIN", admin.getId(), admin.getName(), admin.getEmail());
            }
            case "RETAILER" -> {
                RetailOwner owner = retailerRepo.findByEmail(req.getEmail())
                        .orElseThrow(() -> new RuntimeException("Invalid credentials"));
                if (!encoder.matches(req.getPassword(), owner.getPassword()))
                    throw new RuntimeException("Invalid credentials");
                return new AuthResponse(jwtUtil.generateToken(owner.getEmail(), "RETAILER", owner.getId()),
                        "RETAILER", owner.getId(), owner.getName(), owner.getEmail());
            }
            default -> {
                User user = userRepo.findByEmail(req.getEmail())
                        .orElseThrow(() -> new RuntimeException("Invalid credentials"));
                if (!encoder.matches(req.getPassword(), user.getPassword()))
                    throw new RuntimeException("Invalid credentials");
                return new AuthResponse(jwtUtil.generateToken(user.getEmail(), "USER", user.getId()),
                        "USER", user.getId(), user.getName(), user.getEmail());
            }
        }
    }

    public AuthResponse registerUser(UserRegisterRequest req) {
        if (userRepo.existsByEmail(req.getEmail()))
            throw new RuntimeException("Email already registered");
        User user = User.builder()
                .name(req.getName()).email(req.getEmail())
                .password(encoder.encode(req.getPassword()))
                .phone(req.getPhone()).address(req.getAddress())
                .latitude(req.getLatitude() != null ? BigDecimal.valueOf(req.getLatitude()) : null)
                .longitude(req.getLongitude() != null ? BigDecimal.valueOf(req.getLongitude()) : null)
                .build();
        user = userRepo.save(user);
        return new AuthResponse(jwtUtil.generateToken(user.getEmail(), "USER", user.getId()),
                "USER", user.getId(), user.getName(), user.getEmail());
    }

    public String registerRetailer(RetailerRegisterRequest req) {
        if (retailerRepo.existsByEmail(req.getEmail()))
            throw new RuntimeException("Email already registered");
        RetailOwner owner = RetailOwner.builder()
                .name(req.getName()).email(req.getEmail())
                .password(encoder.encode(req.getPassword()))
                .phone(req.getPhone()).shopName(req.getShopName())
                .shopAddress(req.getShopAddress()).shopCategory(req.getShopCategory())
                .latitude(BigDecimal.valueOf(req.getLatitude()))
                .longitude(BigDecimal.valueOf(req.getLongitude()))
                .gstin(req.getGstin()).isApproved(false)
                .build();
        retailerRepo.save(owner);
        return "Registration successful! Awaiting admin approval.";
    }
}
