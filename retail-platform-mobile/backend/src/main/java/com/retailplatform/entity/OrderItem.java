package com.retailplatform.entity;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity @Table(name = "order_items")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class OrderItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "order_id") private Long orderId;
    @Column(name = "product_id") private Long productId;
    @Column(name = "product_name") private String productName;
    @Column(name = "product_image") private String productImage;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal total;
    @Column(name = "created_at") private LocalDateTime createdAt;
    @PrePersist public void prePersist() { createdAt = LocalDateTime.now(); }
}
