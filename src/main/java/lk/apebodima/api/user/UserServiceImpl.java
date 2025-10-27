package lk.apebodima.api.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDto upgradeTenantToLandlord() {
        // 1. Get the email of the currently logged-in user
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        // 2. Find the user in the database
        User userToUpdate = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + userEmail));

        // 3. Update the user's role
        userToUpdate.setRole(Role.LANDLORD);

        // 4. Save the change back to the database
        User savedUser = userRepository.save(userToUpdate);

        // 5. Return the updated user information
        return mapToUserDto(savedUser);
    }

    // Helper method to convert a User entity to a UserDto
    private UserDto mapToUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .contactNo(user.getContactNo())
                .address(user.getAddress())
                .role(user.getRole())
                .build();
    }
}