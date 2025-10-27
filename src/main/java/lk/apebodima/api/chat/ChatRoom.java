package lk.apebodima.api.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "chat_rooms")
public class ChatRoom {
    @Id
    private String id;
    private String listingId;
    private String senderId;    // The user who initiated the chat (e.g., the tenant)
    private String recipientId; // The user who received the chat request (e.g., the landlord)
}