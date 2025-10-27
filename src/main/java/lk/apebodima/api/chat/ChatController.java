package lk.apebodima.api.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService chatMessageService; // We will create this service

    @MessageMapping("/chat")
    public void processMessage(@Payload ChatMessage chatMessage) {
        ChatMessage savedMsg = chatMessageService.save(chatMessage);

        // Send the message to the recipient via a user-specific topic
        // e.g., /topic/messages/recipientId
        messagingTemplate.convertAndSendToUser(
                savedMsg.getRecipientId(), "/queue/messages", savedMsg
        );
    }
}