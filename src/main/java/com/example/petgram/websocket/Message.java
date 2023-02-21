package com.example.petgram.websocket;

import com.example.petgram.model.User;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("message")
public class Message {
    @Id
    String id;
    private User from;
    private User to;
    private String content;
    private Long chatId;
    private boolean viewed;



    //standard constructors, getters, setters
}