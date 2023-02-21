package com.example.petgram.websocket;

import com.example.petgram.Exception.Status444UserIsNull;
import com.example.petgram.model.User;
import com.example.petgram.service.UserService;
import com.example.petgram.websocket.chatModels.*;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint(
        value="/chat/{chatId}",
        decoders = MessageDecoder.class,
        encoders = MessageEncoder.class )
public class ChatEndpoint {

    private Session session;

    private  User currentUser;
    private final ChatRoomService chatRoomService;
    private final UserService userService;
    private final MessageRepository messageRepository;
    private ChatRoom currentChatRoom;

    private static Set<ChatEndpoint> chatEndpoints
            = new CopyOnWriteArraySet<>();
    private static HashMap<String, Long> users = new HashMap<>();
    public ChatEndpoint(){
        this.chatRoomService = (ChatRoomService) SpringContext.getContext().getBean("chatRoomServiceImpl");
        this.userService = (UserService) SpringContext.getContext().getBean("userServiceImpl");
        this.messageRepository = (MessageRepository) SpringContext.getContext().getBean("messageRepository");
    }


    @OnOpen
    public void onOpen(Session session, @PathParam("chatId") Long chatId) throws Status444UserIsNull {

        this.session = session;
        this.currentUser = userService.getByUserName(session.getUserPrincipal().getName());
        this.currentChatRoom = chatRoomService.getChatRoomByChatIdAndSender(chatId,currentUser);
        users.put(session.getId(), chatId);
        chatEndpoints.add(this);

        for (Message message : messageRepository.findAllByChatIdAndTo(chatId,currentUser)){
                message.setViewed(true);
                messageRepository.save(message);
                broadcast(message);
            }
        }

    @OnMessage
    public void onMessage(Session session, String content,@PathParam("chatId") String chatId) throws Status444UserIsNull {

        ArrayList<User> recipients = new ArrayList<>(currentChatRoom.getRecipients());

        for (User user : recipients){
            Message message = new Message();
            message.setFrom(userService.getByUserName(session.getUserPrincipal().getName()));
            message.setTo(user);
            message.setContent(content);
            message.setChatId(Long.valueOf(chatId));
            message.setViewed(false);
            messageRepository.save(message);
            broadcast(message);

        }
    }

    @OnClose
    public void onClose() {

        chatEndpoints.remove(this);

    }

    @OnError
    public void onError(Throwable throwable) {
        System.out.println(throwable.getMessage());
        // Do error handling here
    }

    private static void broadcast(Message  message) {

        chatEndpoints.forEach(endpoint -> {
            synchronized (endpoint) {
                if (endpoint.currentChatRoom.getChatId().equals(message.getChatId())) {
                    if (message.getTo().equals(endpoint.currentUser)){
                    try {
                        endpoint.session.getBasicRemote().
                                sendObject(message);
                    } catch (IOException | EncodeException e) {
                        e.printStackTrace();
                    }
                }}
            }
        });
    }
}