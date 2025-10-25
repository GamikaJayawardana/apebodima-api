package lk.apebodima.api.repository;

import java.util.Optional;

import lk.apebodima.api.model.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository {
    Optional<User> findByEmail(String email);
    
}
