package lk.apebodima.api.auth;

import lk.apebodima.api.user.UserDto;
import lk.apebodima.api.user.User;
import lk.apebodima.api.user.UserRepository;
import lk.apebodima.api.shared.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    // Inject all the necessary components
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponse register(RegisterRequest request) {
        // Create a new User object from the request DTO
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword())) //Hash the password
                .contactNo(request.getContactNo())
                .address(request.getAddress())
                .role(request.getRole())
                .build();

        //Save the new user to the database
        User savedUser = userRepository.save(user);

        //Generate a JWT for the new user
        var jwtToken = jwtService.generateToken(user);

        //Create a UserDto to send back the user's details safely
        var userDto = UserDto.builder()
                .id(savedUser.getId())
                .firstName(savedUser.getFirstName())
                .lastName(savedUser.getLastName())
                .email(savedUser.getEmail())
                .contactNo(savedUser.getContactNo())
                .address(savedUser.getAddress())
                .role(savedUser.getRole())
                .build();

        //Return the response containing the token and user details
        return AuthResponse.builder()
                .token(jwtToken)
                .user(userDto)
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        // Authenticate the user. Spring Security will check if the email and password are correct.
        // If they are not, it will throw an exception, and the code below won't execute.
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
            )
        );

        //If authentication is successful, find the user in the database
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + request.getEmail()));

        //Generate a JWT for the authenticated user
        var jwtToken = jwtService.generateToken(user);

        //Create a UserDto to send back the user's details safely
        var userDto = UserDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .contactNo(user.getContactNo())
                .address(user.getAddress())
                .role(user.getRole())
                .build();

        //Return the response containing the token and user details
        return AuthResponse.builder()
                .token(jwtToken)
                .user(userDto)
                .build();
    }
}