package com.example.petgram.service;

import com.example.petgram.Exception.Status430UserNotFoundException;
import com.example.petgram.Exception.Status444UserIsNull;
import com.example.petgram.model.ContentType;
import com.example.petgram.security.jwt.UserPrincipal;

public interface NotificationService {

    void sendNotification(UserPrincipal userPrincipal, String recipientUsername, String message, String documentId, ContentType contentType) throws Status444UserIsNull, Status430UserNotFoundException;

}
