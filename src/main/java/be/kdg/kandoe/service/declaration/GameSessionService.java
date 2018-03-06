package be.kdg.kandoe.service.declaration;

import be.kdg.kandoe.domain.GameSession;

import java.util.List;

public interface GameSessionService {
    List<GameSession> getAllGameSessions();
    List<GameSession> getUserGameSessions(String username);
    GameSession addGameSession(GameSession gameSession); //save
    GameSession getGameSessionWithId(Long id);
    GameSession updateGameSession(GameSession gameSession);

}
