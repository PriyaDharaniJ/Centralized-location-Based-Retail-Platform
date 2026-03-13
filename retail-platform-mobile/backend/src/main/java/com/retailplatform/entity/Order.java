package com.retailplatform.entity;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity @Table(name = "orders")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class Order {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "tracking_id", unique = true) private String trackingId;
    @Column(name = "user_id") private Long userId;
    @Column(name = "retail_owner_id") private Long retailOwnerId;
    @Column(name = "order_type") private String orderType;
    private String status;
    private BigDecimal subtotal;
    @Column(name = "delivery_charge") private BigDecimal deliveryCharge;
    private BigDecimal discount;
    @Column(name = "total_amount") private BigDecimal totalAmount;
    @Column(name = "commission_amount") private BigDecimal commissionAmount;
    @Column(name = "payment_method") private String paymentMethod;
    @Column(name = "payment_status") private String paymentStatus;
    @Column(name = "delivery_address") private String deliveryAddress;
    @Column(name = "delivery_latitude") private BigDecimal deliveryLatitude;
    @Column(name = "delivery_longitude") private BigDecimal deliveryLongitude;
    @Column(name = "customer_phone") private String customerPhone;
    private String notes;
    @Column(name = "created_at") private LocalDateTime createdAt;
    @Column(name = "updated_at") private LocalDateTime updatedAt;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    private List<OrderItem> items;
    @PrePersist public void prePersist() { createdAt = updatedAt = LocalDateTime.now(); }
    @PreUpdate public void preUpdate() { updatedAt = LocalDateTime.now(); }
}
