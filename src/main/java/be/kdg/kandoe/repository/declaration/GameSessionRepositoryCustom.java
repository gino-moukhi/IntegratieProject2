package be.kdg.kandoe.repository.declaration;

import be.kdg.kandoe.domain.GameSession;

import java.util.List;

public interface GameSessionRepositoryCustom {
    List<GameSession> findGameSessionsOfUserWithUsername(String username);
}
