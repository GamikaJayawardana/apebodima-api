package lk.apebodima.api.admin;

import lk.apebodima.api.chat.ChatMessage;
import lk.apebodima.api.chat.ChatMessageRepository;
import lk.apebodima.api.chat.ChatRoom;
import lk.apebodima.api.chat.ChatRoomRepository;
import lk.apebodima.api.listing.ListingDto;
import lk.apebodima.api.listing.ListingMapper;
import lk.apebodima.api.listing.ListingRepository;
import lk.apebodima.api.shared.exception.ResourceNotFoundException;
import lk.apebodima.api.user.UserDto;
import lk.apebodima.api.user.UserMapper;
import lk.apebodima.api.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import lk.apebodima.api.user.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final ListingRepository listingRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserMapper userMapper;
    private final ListingMapper listingMapper;

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(String userId) {
        return userRepository.findById(userId)
                .map(userMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
    }

    @Override
    public List<ListingDto> getAllListings() {
        return listingRepository.findAll().stream()
                .map(listingMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ListingDto> getBoostedListings() {
        return listingRepository.findAllByIsBoostedIsTrue().stream()
                .map(listingMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ChatRoom> getAllChatRooms() {
        return chatRoomRepository.findAll();
    }

    @Override
    public List<ChatMessage> getMessagesForChatRoom(String chatRoomId) {
        return chatMessageRepository.findByChatRoomId(chatRoomId);
    }

    @Override
    public void disableUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        user.setEnabled(false);
        userRepository.save(user);
    }

    @Override
    public void enableUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        user.setEnabled(true);
        userRepository.save(user);
    }
}