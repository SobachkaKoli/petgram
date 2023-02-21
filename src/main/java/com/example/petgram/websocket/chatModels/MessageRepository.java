package com.example.petgram.websocket.chatModels;

import com.example.petgram.model.User;
import com.example.petgram.websocket.Message;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MessageRepository extends MongoRepository<Message,String> {

    List<Message> findAllByChatIdAndTo(Long chatId, User user);

}
