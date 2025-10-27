package lk.apebodima.api.admin;

import lk.apebodima.api.user.UserDto;
import java.util.List;

public interface AdminService {
    List<UserDto> getAllUsers();
}