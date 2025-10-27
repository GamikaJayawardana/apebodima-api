package lk.apebodima.api.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String id; // <-- Changed from Long to String
    private String firstName;
    private String lastName;
    private String email;
    private String contactNo;
    private String address;
    private Role role;
}