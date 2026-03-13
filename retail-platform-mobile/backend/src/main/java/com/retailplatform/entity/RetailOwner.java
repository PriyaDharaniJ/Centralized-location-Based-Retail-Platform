package com.retailplatform.entity;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity @Table(name = "retail_owners")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class RetailOwner {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    private String name;
    @Column(unique = true) private String email;
    private String password;
    private String phone;
    @Column(name = "shop_name") private String shopName;
    @Column(name = "shop_address") private String shopAddress;
    @Column(name = "shop_category") private String shopCategory;
    private BigDecimal latitude;
    private BigDecimal longitude;
    @Column(name = "shop_image") private String shopImage;
    private String gstin;
    @Column(name = "is_open") private Boolean isOpen = false;
    @Column(name = "delivery_available") private Boolean deliveryAvailable = false;
    @Column(name = "is_approved") private Boolean isApproved = false;
    @Column(name = "is_active") private Boolean isActive = true;
    private BigDecimal rating = BigDecimal.ZERO;
    @Column(name = "total_orders") private Integer totalOrders = 0;
    @Column(name = "total_revenue") private BigDecimal totalRevenue = BigDecimal.ZERO;
    @Column(name = "commission_rate") private BigDecimal commissionRate = new BigDecimal("5.00");
    @Column(name = "created_at") private LocalDateTime createdAt;
    @Column(name = "updated_at") private LocalDateTime updatedAt;
    @PrePersist public void prePersist() {
        createdAt = updatedAt = LocalDateTime.now();
        if (isOpen == null) isOpen = false;
        if (deliveryAvailable == null) deliveryAvailable = false;
        if (isApproved == null) isApproved = false;
        if (isActive == null) isActive = true;
        if (totalOrders == null) totalOrders = 0;
        if (totalRevenue == null) totalRevenue = BigDecimal.ZERO;
        if (commissionRate == null) commissionRate = new BigDecimal("5.00");
    }
    @PreUpdate public void preUpdate() { updatedAt = LocalDateTime.now(); }
}
