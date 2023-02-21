package com.example.petgram.controller;

import com.example.petgram.Exception.Status444UserIsNull;
import com.example.petgram.Exception.Status446ChatRoomAlreadyExsists;
import com.example.petgram.security.JwtUser;
import com.example.petgram.websocket.chatModels.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class ChatController {

    private final ChatRoomService chatRoomService;



    @PostMapping("/create-chat/")
    public Long createChat(@RequestParam List<String> usernames, JwtUser jwtUser) throws Status444UserIsNull, Status446ChatRoomAlreadyExsists {
     return chatRoomService.createChatRoom(jwtUser,usernames);

    }


//    @GetMapping("/max")
//    public String[] getMaxId(){
//        return new String[]{
//                chatRoomRepository.findTopByOrderByChatIdAsc().getChatId(),
//                chatRoomRepository.findTopByOrderByChatIdDesc().getChatId()};
//    }
}
