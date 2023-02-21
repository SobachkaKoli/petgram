package com.example.petgram.websocket.chatModels;

import com.example.petgram.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRoomRepository extends MongoRepository<ChatRoom,String> {

    Optional<ChatRoom> findTopByOrderByChatIdDesc();
    ChatRoom findChatRoomByChatIdAndSender(Long chatId, User sender);
}
