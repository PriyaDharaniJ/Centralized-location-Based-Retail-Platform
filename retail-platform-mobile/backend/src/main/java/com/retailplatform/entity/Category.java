package com.retailplatform.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity @Table(name = "categories")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class Category {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    private String name;
    private String icon;
    private String description;
    @Column(name = "is_active") private Boolean isActive = true;
    @Column(name = "created_at") private LocalDateTime createdAt;
    @PrePersist public void prePersist() { createdAt = LocalDateTime.now(); }
}
