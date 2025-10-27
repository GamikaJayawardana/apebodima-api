package lk.apebodima.api.admin;

import lk.apebodima.api.user.UserDto;
import lk.apebodima.api.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;

    @Override
    public List<UserDto> getAllUsers() {
        // Fetch all users from the database
        return userRepository.findAll().stream()
                // Map each User entity to a UserDto
                .map(user -> UserDto.builder()
                        .id(user.getId())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .email(user.getEmail())
                        .contactNo(user.getContactNo())
                        .address(user.getAddress())
                        .role(user.getRole())
                        .build())
                .collect(Collectors.toList());
    }
}