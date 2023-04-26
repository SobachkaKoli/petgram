package com.example.petgram.websocket.chatModels;

import com.example.petgram.Exception.Status430UserNotFoundException;
import com.example.petgram.Exception.Status444UserIsNull;
import com.example.petgram.model.User;
import com.example.petgram.security.jwt.UserPrincipal;
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


    private void saveChatRoom(Long chatId, UserPrincipal userPrincipal, List<String> usernames) throws Status444UserIsNull, Status430UserNotFoundException {

        ArrayList<User> members = new ArrayList<>();
        members.add(userService.getAuthenticatedUser(userPrincipal));
        for (String username : usernames) {
            members.add(userService.findByUsername(username));
        }
        //  TODO (Bogdan O.) 26/4/23: how does it work?
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
    public Long createChatRoom(UserPrincipal userPrincipal, List<String> usernames) throws Status444UserIsNull, Status430UserNotFoundException {

        if (chatRoomRepository.findTopByOrderByChatIdDesc().isPresent()) {
//            if (chatRoomRepository.existsBySenderAndRecipient(userService.getAuthenticatedUser(token), userService.getByUserName(usernames))) {
//              //TODO
//                throw new Status446ChatRoomAlreadyExsists("chatRoomAlreadyExists");
//            }else {
            Long chatId = chatRoomRepository.findTopByOrderByChatIdDesc().get().getChatId() + 1L;
            saveChatRoom(chatId, userPrincipal, usernames);
            System.out.println(chatId);
            return chatId;
//            }
        } else {
            Long chatId = 0L;
            saveChatRoom(chatId, userPrincipal, usernames);
            return chatId;
        }


    }

    @Override
    public ChatRoom getChatRoomByChatIdAndSender(Long chatId, User sender) {
//        System.out.println(chatRoomRepository.findChatRoomByChatIdAndSender(chatId, sender));
        return chatRoomRepository.findChatRoomByChatIdAndSender(chatId, sender);
    }


}
