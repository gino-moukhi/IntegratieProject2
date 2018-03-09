package be.kdg.kandoe.service.implementation;

import be.kdg.kandoe.domain.GameSession;
import be.kdg.kandoe.repository.declaration.GameSessionRepository;
import be.kdg.kandoe.service.declaration.GameSessionService;
import be.kdg.kandoe.service.exception.GameSessionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameSessionServiceImpl implements GameSessionService{


    private GameSessionRepository gameSessionRepository;

    @Autowired
    public GameSessionServiceImpl(GameSessionRepository gameSessionRepository) {
        this.gameSessionRepository = gameSessionRepository;
    }

    @Override
    public List<GameSession> getAllGameSessions() {
        return gameSessionRepository.findAll();
    }

    @Override
    public List<GameSession> getUserGameSessions(String username) {
        return gameSessionRepository.findGameSessionsOfUserWithUsername(username);
    }

    @Override
    public GameSession addGameSession(GameSession gameSession) {
        return gameSessionRepository.save(gameSession);
    }

    @Override
    public GameSession getGameSessionWithId(Long id) {
        return gameSessionRepository.findOne(id);
    }

    @Override
    public GameSession updateGameSession(GameSession gameSession) {
        GameSession u = gameSessionRepository.save(gameSession);
        if (u == null)
            throw new GameSessionException("User not saved");
        return u;
    }
}
