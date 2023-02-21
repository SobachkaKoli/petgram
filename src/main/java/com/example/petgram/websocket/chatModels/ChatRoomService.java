package com.example.petgram.websocket.chatModels;

import com.example.petgram.Exception.Status444UserIsNull;
import com.example.petgram.Exception.Status446ChatRoomAlreadyExsists;
import com.example.petgram.model.User;
import com.example.petgram.security.JwtUser;

import java.util.List;


public interface ChatRoomService {
    Long createChatRoom(JwtUser jwtUser, List<String> usernames) throws Status444UserIsNull, Status446ChatRoomAlreadyExsists;

    ChatRoom getChatRoomByChatIdAndSender(Long chatId, User sender);

}
