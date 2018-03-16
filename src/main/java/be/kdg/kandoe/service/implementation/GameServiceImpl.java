package be.kdg.kandoe.service.implementation;

import be.kdg.kandoe.domain.Vote;
import be.kdg.kandoe.domain.user.User;
import be.kdg.kandoe.repository.declaration.GameRepository;
import be.kdg.kandoe.service.declaration.GameService;
import be.kdg.kandoe.service.declaration.GameSessionService;
import be.kdg.kandoe.service.declaration.ThemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameServiceImpl implements GameService{

    private ThemeService themeService;
    private GameSessionService gameSessionService;
    private GameRepository gameRepository;

    @Autowired
    public GameServiceImpl(ThemeService themeService, GameSessionService gameSessionService, GameRepository gameRepository) {
        this.themeService = themeService;
        this.gameSessionService = gameSessionService;
        this.gameRepository = gameRepository;
    }

    @Override
    public void startSession(long sessionId) {

    }

    @Override
    public Vote MakeVote(long sessionId, long cardId, User user) {
        Vote vote = new Vote();
        vote.setCard(themeService.getCardById(cardId));
        vote.setGameSession(gameSessionService.getGameSessionWithId(sessionId));
        return gameRepository.addVote(vote);
    }
}