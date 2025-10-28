package lk.apebodima.api.admin;

import lk.apebodima.api.chat.ChatMessage;
import lk.apebodima.api.chat.ChatRoom;
import lk.apebodima.api.listing.ListingDto;
import lk.apebodima.api.user.UserDto;
import java.util.List;

public interface AdminService {
    List<UserDto> getAllUsers();
    UserDto getUserById(String userId);
    List<ListingDto> getAllListings();
    List<ListingDto> getBoostedListings();
    List<ChatRoom> getAllChatRooms();
    List<ChatMessage> getMessagesForChatRoom(String chatRoomId);
    void disableUser(String userId);
    void enableUser(String userId);
}