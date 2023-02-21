package com.example.petgram.websocket.chatModels;

import com.example.petgram.model.User;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@Document("chatRoom")
public class ChatRoom {

    @Id
    private String id;

    private Long chatId;

    private User sender;

    private List<User> recipients;






}
