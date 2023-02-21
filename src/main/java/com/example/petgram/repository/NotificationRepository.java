package com.example.petgram.repository;

import com.example.petgram.model.User;
import com.example.petgram.notifications.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends MongoRepository<Notification,String > {
    List<Notification> findAllByRecipient(User recipient);
}
