package com.retailplatform.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity @Table(name = "cart")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class Cart {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "user_id") private Long userId;
    @Column(name = "product_id") private Long productId;
    private Integer quantity;
    @Column(name = "created_at") private LocalDateTime createdAt;
    @Column(name = "updated_at") private LocalDateTime updatedAt;
    @PrePersist public void prePersist() { createdAt = updatedAt = LocalDateTime.now(); }
    @PreUpdate public void preUpdate() { updatedAt = LocalDateTime.now(); }
}
