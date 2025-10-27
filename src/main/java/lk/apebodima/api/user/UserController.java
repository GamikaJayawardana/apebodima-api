package lk.apebodima.api.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/upgrade-to-landlord")
    @PreAuthorize("hasAuthority('TENANT')") // Only users with the TENANT role can call this
    public ResponseEntity<UserDto> upgradeToLandlord() {
        UserDto updatedUser = userService.upgradeTenantToLandlord();
        return ResponseEntity.ok(updatedUser);
    }
}