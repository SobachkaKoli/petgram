package com.example.petgram.notifications;

import com.example.petgram.Exception.Status430UserNotFoundException;
import com.example.petgram.Exception.Status444UserIsNull;
import com.example.petgram.model.ContentType;
import com.example.petgram.repository.NotificationRepository;
import com.example.petgram.security.jwt.UserPrincipal;
import com.example.petgram.service.NotificationService;
import com.example.petgram.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    private final UserService userService;

    public NotificationServiceImpl(NotificationRepository notificationRepository, UserService userService) {
        this.notificationRepository = notificationRepository;
        this.userService = userService;
    }
    @Override
    public void sendNotification(UserPrincipal userPrincipal, String recipientUsername, String message, String documentId, ContentType contentType) throws Status444UserIsNull, Status430UserNotFoundException {
        Notification notification = Notification.builder()
                .sender(userService.getAuthenticatedUser(userPrincipal))
                .message(message)
                .documentId(documentId)
                .contentType(contentType)
                .recipient(userService.getByUsername(recipientUsername))
                .build();
        notificationRepository.save(notification);
    }

}
