package be.kdg.kandoe.repository.declaration;

import be.kdg.kandoe.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {
    User findUserByUsername(String username);
    User findUserByEmail(String email);
//    @Query(value = "SELECT u.username FROM USERS u WHERE UPPER(u.USERNAME) = UPPER(:username)", nativeQuery = true)
//    User findUserByUsername(@Param("username") String username);
//    User findUserByEmail(String email);
}
