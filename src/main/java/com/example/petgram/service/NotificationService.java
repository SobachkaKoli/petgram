package com.example.petgram.service;

import com.example.petgram.Exception.Status444UserIsNull;
import com.example.petgram.model.ContentType;
import com.example.petgram.security.JwtUser;

public interface NotificationService {

    void sendNotification(JwtUser jwtUser, String recipientUsername, String message, String documentId, ContentType contentType) throws Status444UserIsNull;

}
