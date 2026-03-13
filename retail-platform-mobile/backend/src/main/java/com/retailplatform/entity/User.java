package com.retailplatform.entity;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity @Table(name = "users")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    private String name;
    @Column(unique = true) private String email;
    private String password;
    private String phone;
    private String address;
    private BigDecimal latitude;
    private BigDecimal longitude;
    @Column(name = "profile_image") private String profileImage;
    @Column(name = "is_active") private Boolean isActive = true;
    @Column(name = "created_at") private LocalDateTime createdAt;
    @Column(name = "updated_at") private LocalDateTime updatedAt;
    @PrePersist public void prePersist() { createdAt = updatedAt = LocalDateTime.now(); isActive = true; }
    @PreUpdate public void preUpdate() { updatedAt = LocalDateTime.now(); }
}
