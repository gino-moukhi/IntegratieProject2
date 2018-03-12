package be.kdg.kandoe.repository.declaration;

import be.kdg.kandoe.domain.GameSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameSessionRepository extends JpaRepository<GameSession, Long>, GameSessionRepositoryCustom {
    GameSession findGameSessionByGameSessionId(Long id);
}
