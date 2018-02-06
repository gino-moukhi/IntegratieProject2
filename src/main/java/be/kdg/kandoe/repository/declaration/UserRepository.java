package be.kdg.kandoe.repository.declaration;

import be.kdg.kandoe.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {
    User findByEmail(String email);
    User findByUsername(String username);
    User findUserByUsername(String username);
}
