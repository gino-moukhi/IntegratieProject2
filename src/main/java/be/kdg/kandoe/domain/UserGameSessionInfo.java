package be.kdg.kandoe.domain;

import be.kdg.kandoe.domain.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
@Table
public class UserGameSessionInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false,columnDefinition = "serial")
    private Long userGameSessionInfoId;

    @Column(nullable = false)
    @ElementCollection(targetClass = Notification.class)
    @JoinTable(joinColumns = @JoinColumn(name = "userGameSessionInfoId"))
    @Enumerated(EnumType.STRING)
    private List<Notification> notifications;

    @Column(nullable = false)
    private boolean voted;

    @Column(nullable = false)
    private GameSessionRole role;

    @ManyToOne(targetEntity = User.class,fetch = FetchType.EAGER)
    @JsonIgnore
    private User user;

    @ManyToOne(targetEntity = GameSession.class,fetch = FetchType.EAGER)
    @JsonIgnore
    private GameSession gameSession;

    public UserGameSessionInfo() {
    }

    public UserGameSessionInfo(List<Notification> notifications, boolean voted, GameSessionRole role, User user, GameSession gameSession) {
        this.notifications = notifications;
        this.voted = voted;
        this.role = role;
        this.user = user;
        this.gameSession = gameSession;
    }

    public Long getUserGameSessionInfoId() {
        return userGameSessionInfoId;
    }

    public void setUserGameSessionInfoId(Long userGameSessionInfoId) {
        this.userGameSessionInfoId = userGameSessionInfoId;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }

    public boolean isVoted() {
        return voted;
    }

    public void setVoted(boolean voted) {
        this.voted = voted;
    }

    public GameSessionRole getRole() {
        return role;
    }

    public void setRole(GameSessionRole role) {
        this.role = role;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public GameSession getGameSession() {
        return gameSession;
    }

    public void setGameSession(GameSession gameSession) {
        this.gameSession = gameSession;
    }
}
