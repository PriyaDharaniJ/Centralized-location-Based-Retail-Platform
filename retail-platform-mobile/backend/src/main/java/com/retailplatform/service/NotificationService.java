package com.retailplatform.service;
import com.retailplatform.entity.Notification;
import com.retailplatform.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class NotificationService {
    @Autowired private NotificationRepository notifRepo;
    @Autowired private SimpMessagingTemplate messagingTemplate;

    public void sendNotification(String recipientType, Long recipientId, String title, String message, String type, Long referenceId) {
        Notification notif = Notification.builder()
                .recipientType(recipientType).recipientId(recipientId)
                .title(title).message(message).type(type).referenceId(referenceId)
                .build();
        notif = notifRepo.save(notif);
        // Send via WebSocket
        messagingTemplate.convertAndSend("/topic/notifications/" + recipientType.toLowerCase() + "/" + recipientId, notif);
    }

    public List<Notification> getNotifications(String type, Long id) {
        return notifRepo.findByRecipientTypeAndRecipientIdOrderByCreatedAtDesc(type, id);
    }

    public long getUnreadCount(String type, Long id) {
        return notifRepo.countByRecipientTypeAndRecipientIdAndIsReadFalse(type, id);
    }

    public void markAllRead(String type, Long id) {
        notifRepo.markAllAsRead(type, id);
    }
}
