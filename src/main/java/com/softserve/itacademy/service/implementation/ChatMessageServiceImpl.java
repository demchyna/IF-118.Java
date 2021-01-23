package com.softserve.itacademy.service.implementation;

import com.softserve.itacademy.entity.ChatMessage;
import com.softserve.itacademy.entity.User;
import com.softserve.itacademy.exception.NotFoundException;
import com.softserve.itacademy.projection.ChatMessageTinyProjection;
import com.softserve.itacademy.repository.ChatMessageRepository;
import com.softserve.itacademy.request.ChatMessageRequest;
import com.softserve.itacademy.response.ChatMessageResponse;
import com.softserve.itacademy.service.ChatMessageService;
import com.softserve.itacademy.service.ChatRoomService;
import com.softserve.itacademy.service.GroupService;
import com.softserve.itacademy.service.UserService;
import com.softserve.itacademy.service.converters.ChatMessageConverter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ChatMessageServiceImpl implements ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final GroupService groupService;
    private final ChatRoomService groupChatService;
    private final UserService userService;
    private final ChatMessageConverter chatMessageConverter;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatMessageServiceImpl(ChatMessageRepository chatMessageRepository, GroupService groupService, ChatRoomService groupChatService, UserService userService, ChatMessageConverter chatMessageConverter, SimpMessagingTemplate messagingTemplate) {
        this.chatMessageRepository = chatMessageRepository;
        this.groupService = groupService;
        this.groupChatService = groupChatService;
        this.userService = userService;
        this.chatMessageConverter = chatMessageConverter;
        this.messagingTemplate = messagingTemplate;
    }

    @Transactional
    @Override
    public ChatMessageResponse processMessage(ChatMessageRequest chatMessageRequest, User user) {

        ChatMessage chatMessage = ChatMessage.builder()
                .content(chatMessageRequest.getContent())
                .chatRoom(groupChatService.getById(chatMessageRequest.getGroupId()))
                .user(userService.getById(user.getId()))
                .status(ChatMessage.MessageStatus.RECEIVED)
                .build();

        save(chatMessage);

        return sendMessage(chatMessage.getId());
    }

    private ChatMessageResponse sendMessage(Integer messageId) {
        ChatMessage chatMessage = chatMessageRepository.findById(messageId).orElseThrow(NotFoundException::new);

        ChatMessageResponse response = chatMessageConverter.of(chatMessage);
        response.setUser(userService.findById(chatMessage.getUser().getId()));

        messagingTemplate.convertAndSend("/api/v1/ws/event/chat/group/" + chatMessage.getChatRoom().getGroup().getId(), response);
        chatMessage.setStatus(ChatMessage.MessageStatus.DELIVERED);
        chatMessageRepository.save(chatMessage);

        return response;
    }

    @Override
    public ChatMessage save(ChatMessage chatMessage) {
        chatMessageRepository.save(chatMessage);
        return chatMessage;
    }

    @Override
    public List<ChatMessageTinyProjection> findPaginatedByChatRoomId(int pageNo, int pageSize, int chatId) {

        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by("createdAt").descending());
        Page<ChatMessageTinyProjection> pagedResult = chatMessageRepository.findPaginatedByChatRoomId(paging, chatId);

        List<ChatMessageTinyProjection> list = new ArrayList<>(pagedResult.toList());
        Collections.reverse(list);

        return list;
    }

}
