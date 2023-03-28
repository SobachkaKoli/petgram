package com.example.petgram.websocket.chatModels;

import com.example.petgram.Exception.Status430UserNotFoundException;
import com.example.petgram.Exception.Status444UserIsNull;
import com.example.petgram.Exception.Status446ChatRoomAlreadyExsists;
import com.example.petgram.model.User;
import com.example.petgram.security.jwt.UserPrincipal;

import java.util.List;


public interface ChatRoomService {
    Long createChatRoom(UserPrincipal userPrincipal, List<String> usernames) throws Status444UserIsNull, Status446ChatRoomAlreadyExsists, Status430UserNotFoundException;

    ChatRoom getChatRoomByChatIdAndSender(Long chatId, User sender);

}
