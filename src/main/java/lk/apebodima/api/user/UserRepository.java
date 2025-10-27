package lk.apebodima.api.user;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
// Extend MongoRepository and use String for the ID type
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
}