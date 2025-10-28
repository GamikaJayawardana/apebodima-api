// In: src/main/java/lk/apebodima/api/user/UserController.java
package lk.apebodima.api.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/upgrade-to-landlord")
    @PreAuthorize("hasAuthority('TENANT')")
    public ResponseEntity<UserDto> upgradeToLandlord() {
        UserDto updatedUser = userService.upgradeTenantToLandlord();
        return ResponseEntity.ok(updatedUser);
    }

    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(@RequestBody @Valid ChangePasswordRequest request) {
        userService.changePassword(request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/me")
    public ResponseEntity<UserDto> updateMyProfile(@RequestBody @Valid UpdateUserRequest request) {
        return ResponseEntity.ok(userService.updateMyProfile(request));
    }
}