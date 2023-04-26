package com.example.petgram.controller;

import com.example.petgram.Exception.Status430UserNotFoundException;
import com.example.petgram.Exception.Status444UserIsNull;
import com.example.petgram.Exception.Status446ChatRoomAlreadyExsists;
import com.example.petgram.security.jwt.UserPrincipal;
import com.example.petgram.websocket.chatModels.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//  TODO (Bogdan O.) 26/4/23: rename endpoint
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class ChatController {

    private final ChatRoomService chatRoomService;


    //  TODO (Bogdan O.) 26/4/23: use proper CRUD namings
    @PostMapping("/create-chat/")
    public Long createChat(@RequestParam List<String> usernames, UserPrincipal userPrincipal) throws Status444UserIsNull, Status446ChatRoomAlreadyExsists, Status430UserNotFoundException {
     return chatRoomService.createChatRoom(userPrincipal,usernames);

    }


//    @GetMapping("/max")
//    public String[] getMaxId(){
//        return new String[]{
//                chatRoomRepository.findTopByOrderByChatIdAsc().getChatId(),
//                chatRoomRepository.findTopByOrderByChatIdDesc().getChatId()};
//    }
}
