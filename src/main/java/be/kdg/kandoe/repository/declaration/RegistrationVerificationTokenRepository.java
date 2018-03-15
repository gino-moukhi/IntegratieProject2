package be.kdg.kandoe.repository.declaration;

import be.kdg.kandoe.domain.RegistrationVerificationToken;
import be.kdg.kandoe.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistrationVerificationTokenRepository extends JpaRepository<RegistrationVerificationToken, Long> {
    RegistrationVerificationToken findByToken(String token);

    RegistrationVerificationToken findByUser(User user);
}
