package com.retailplatform.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity @Table(name = "admins")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class Admin {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    private String name;
    @Column(unique = true) private String email;
    private String password;
    private String phone;
    @Column(name = "created_at") private LocalDateTime createdAt;
    @Column(name = "updated_at") private LocalDateTime updatedAt;
    @PrePersist public void prePersist() { createdAt = updatedAt = LocalDateTime.now(); }
    @PreUpdate public void preUpdate() { updatedAt = LocalDateTime.now(); }
}
