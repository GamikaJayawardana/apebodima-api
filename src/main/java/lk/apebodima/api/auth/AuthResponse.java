package lk.apebodima.api.auth;

import lk.apebodima.api.user.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String token; // JWT token for authentication
    private UserDto user; // User details
}
