package com.retailplatform.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity @Table(name = "wishlist")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class Wishlist {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "user_id") private Long userId;
    @Column(name = "product_id") private Long productId;
    @Column(name = "created_at") private LocalDateTime createdAt;
    @PrePersist public void prePersist() { createdAt = LocalDateTime.now(); }
}
