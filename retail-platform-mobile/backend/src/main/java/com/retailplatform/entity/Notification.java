package com.retailplatform.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity @Table(name = "notifications")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class Notification {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "recipient_type") private String recipientType;
    @Column(name = "recipient_id") private Long recipientId;
    private String title;
    private String message;
    private String type;
    @Column(name = "reference_id") private Long referenceId;
    @Column(name = "is_read") private Boolean isRead = false;
    @Column(name = "created_at") private LocalDateTime createdAt;
    @PrePersist public void prePersist() { createdAt = LocalDateTime.now(); isRead = false; }
}
