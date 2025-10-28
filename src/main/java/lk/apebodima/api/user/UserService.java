package lk.apebodima.api.user;

public interface UserService {
    UserDto upgradeTenantToLandlord();
    void changePassword(ChangePasswordRequest request);
    UserDto updateMyProfile(UpdateUserRequest request);
}