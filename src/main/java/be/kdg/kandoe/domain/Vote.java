package be.kdg.kandoe.domain;

import be.kdg.kandoe.domain.theme.Card;
import be.kdg.kandoe.domain.user.User;

import java.time.LocalDateTime;

public class Vote {
    private long voteId;
    private Card card;
    private GameSession gameSession;
    private User user;
    private LocalDateTime time;

    public Vote() {
    }

    public Vote(long voteId, Card card, GameSession gameSession, User user, LocalDateTime time) {
        this.voteId = voteId;
        this.card = card;
        this.gameSession = gameSession;
        this.user = user;
        this.time = time;
    }

    public long getVoteId() {
        return voteId;
    }

    public void setVoteId(long voteId) {
        this.voteId = voteId;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
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