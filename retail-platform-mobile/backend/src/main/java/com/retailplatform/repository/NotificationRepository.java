package com.retailplatform.repository;
import com.retailplatform.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByRecipientTypeAndRecipientIdOrderByCreatedAtDesc(String type, Long id);
    long countByRecipientTypeAndRecipientIdAndIsReadFalse(String type, Long id);
    @Modifying @Transactional
    @Query("UPDATE Notification n SET n.isRead = true WHERE n.recipientType = :type AND n.recipientId = :id")
    void markAllAsRead(@Param("type") String type, @Param("id") Long id);
}
