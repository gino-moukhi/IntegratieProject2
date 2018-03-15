package be.kdg.kandoe.domain;

import be.kdg.kandoe.domain.theme.Card;
import be.kdg.kandoe.domain.user.User;

public class Vote {
    private Card card;
    private GameSession gameSession;
    private User user;

    public Vote() {
    }

    public Vote(Card card, GameSession gameSession, User user) {
        this.card = card;
        this.gameSession = gameSession;
        this.user = user;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public GameSession getGameSession() {
        return gameSession;
    }

    public void setGameSession(GameSession gameSession) {
        this.gameSession = gameSession;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}