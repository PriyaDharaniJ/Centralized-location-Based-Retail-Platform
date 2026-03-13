package com.retailplatform.entity;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity @Table(name = "products")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class Product {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "retail_owner_id") private Long retailOwnerId;
    @Column(name = "category_id") private Long categoryId;
    private String name;
    private String description;
    private BigDecimal price;
    @Column(name = "original_price") private BigDecimal originalPrice;
    private Integer quantity;
    private String unit;
    private String brand;
    private String sku;
    @Column(name = "image_url") private String imageUrl;
    @Column(name = "is_available") private Boolean isAvailable = true;
    @Column(name = "pickup_available") private Boolean pickupAvailable = true;
    @Column(name = "delivery_available") private Boolean deliveryAvailable = true;
    private BigDecimal rating = BigDecimal.ZERO;
    @Column(name = "review_count") private Integer reviewCount = 0;
    @Column(name = "sold_count") private Integer soldCount = 0;
    @Column(name = "discount_percent") private BigDecimal discountPercent = BigDecimal.ZERO;
    @Column(name = "created_at") private LocalDateTime createdAt;
    @Column(name = "updated_at") private LocalDateTime updatedAt;
    @PrePersist public void prePersist() { createdAt = updatedAt = LocalDateTime.now(); }
    @PreUpdate public void preUpdate() { updatedAt = LocalDateTime.now(); }
}
