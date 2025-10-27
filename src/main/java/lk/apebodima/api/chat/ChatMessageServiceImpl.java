// Create this new file in: src/main/java/lk/apebodima/api/chat/ChatMessageServiceImpl.java
package lk.apebodima.api.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {

    private final ChatMessageRepository messageRepository;
    private final ChatRoomRepository chatRoomRepository;

    @Override
    public ChatMessage save(ChatMessage chatMessage) {
        // Find or create a chat room ID for the sender and recipient
        var chatRoomId = chatRoomRepository.findBySenderIdAndRecipientId(chatMessage.getSenderId(), chatMessage.getRecipientId())
                .map(ChatRoom::getId)
                .orElseGet(() -> {
                    ChatRoom newChatRoom = ChatRoom.builder()
                            .senderId(chatMessage.getSenderId())
                            .recipientId(chatMessage.getRecipientId())
                            .build();
                    ChatRoom savedChatRoom = chatRoomRepository.save(newChatRoom);
                    return savedChatRoom.getId();
                });

        chatMessage.setChatRoomId(chatRoomId);
        return messageRepository.save(chatMessage);
    }
}