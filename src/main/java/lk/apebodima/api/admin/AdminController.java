package lk.apebodima.api.admin;

import lk.apebodima.api.chat.ChatMessage;
import lk.apebodima.api.chat.ChatRoom;
import lk.apebodima.api.listing.ListingDto;
import lk.apebodima.api.user.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @GetMapping("/users/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<UserDto> getUserById(@PathVariable String userId) {
        return ResponseEntity.ok(adminService.getUserById(userId));
    }

    @GetMapping("/listings")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<ListingDto>> getAllListings() {
        return ResponseEntity.ok(adminService.getAllListings());
    }

    @GetMapping("/listings/boosted")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<ListingDto>> getBoostedListings() {
        return ResponseEntity.ok(adminService.getBoostedListings());
    }

    @GetMapping("/chats")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<ChatRoom>> getAllChatRooms() {
        return ResponseEntity.ok(adminService.getAllChatRooms());
    }

    @GetMapping("/chats/{chatRoomId}/messages")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<ChatMessage>> getMessagesForChatRoom(@PathVariable String chatRoomId) {
        return ResponseEntity.ok(adminService.getMessagesForChatRoom(chatRoomId));
    }

    @PostMapping("/users/{userId}/disable")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> disableUser(@PathVariable String userId) {
        adminService.disableUser(userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/users/{userId}/enable")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> enableUser(@PathVariable String userId) {
        adminService.enableUser(userId);
        return ResponseEntity.ok().build();
    }
}