package com.example.petgram.websocket.chatModels;

import com.example.petgram.Exception.Status444UserIsNull;
import com.example.petgram.Exception.Status446ChatRoomAlreadyExsists;
import com.example.petgram.model.User;
import com.example.petgram.security.JwtUser;
import com.example.petgram.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final UserService userService;


    private void saveChatRoom(Long chatId, JwtUser jwtUser, List<String> usernames) throws Status444UserIsNull {

        ArrayList<User> members = new ArrayList<>();
        members.add(userService.getAuthenticatedUser(jwtUser));
        for (String username : usernames) {
            members.add(userService.getByUserName(username));
        }

        for (User user : members) {

            ArrayList<User> recipients = new ArrayList<>(members);
            recipients.remove(user);

            ChatRoom chatRoomSender = ChatRoom.builder()
                    .chatId(chatId)
                    .sender(user)
                    .recipients(recipients)
                    .build();
            chatRoomRepository.save(chatRoomSender);
        }
    }

    @Override
    public Long createChatRoom(JwtUser jwtUser, List<String> usernames) throws Status444UserIsNull{

        if (chatRoomRepository.findTopByOrderByChatIdDesc().isPresent()) {
//            if (chatRoomRepository.existsBySenderAndRecipient(userService.getAuthenticatedUser(token), userService.getByUserName(usernames))) {
//              //TODO
//                throw new Status446ChatRoomAlreadyExsists("chatRoomAlreadyExists");
//            }else {
            Long chatId = chatRoomRepository.findTopByOrderByChatIdDesc().get().getChatId() + 1L;
            saveChatRoom(chatId, jwtUser, usernames);
            System.out.println(chatId);
            return chatId;
//            }
        } else {
            Long chatId = 0L;
            saveChatRoom(chatId, jwtUser, usernames);
            return chatId;
        }


    }

    @Override
    public ChatRoom getChatRoomByChatIdAndSender(Long chatId, User sender) {
//        System.out.println(chatRoomRepository.findChatRoomByChatIdAndSender(chatId, sender));
        return chatRoomRepository.findChatRoomByChatIdAndSender(chatId, sender);
    }


}
