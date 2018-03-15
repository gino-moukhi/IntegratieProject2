package be.kdg.kandoe.repository.jpa;

import be.kdg.kandoe.domain.GameSession;
import be.kdg.kandoe.domain.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="VOTE")
public class VoteJpa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "voteId", nullable=false)
    private long voteId;

    @ManyToOne(targetEntity = CardJpa.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "cardId_FK")
    private CardJpa card;

    @ManyToOne(targetEntity = GameSession.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "gameSessionId_FK")
    private GameSession gameSession;


    @ManyToOne(targetEntity = User.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "userId_FK")
    private User user;

    @Column(nullable = false)
    private LocalDateTime time;


    public VoteJpa() {
    }

    public VoteJpa(CardJpa card, GameSession gameSession, User user, LocalDateTime time) {
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

    public CardJpa getCard() {
        return card;
    }

    public void setCard(CardJpa card) {
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

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}