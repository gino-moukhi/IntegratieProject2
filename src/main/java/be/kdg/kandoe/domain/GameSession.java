package be.kdg.kandoe.domain;

import be.kdg.kandoe.domain.user.User;
import be.kdg.kandoe.dto.gameSession.CreateGameSessionDto;
import org.hibernate.annotations.Fetch;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class GameSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long gameSessionId;

    @Column(nullable = false)
    @Value("${gamesession.default.isOrganisatorPlaying}")
    private boolean isOrganisatorPlaying;

    @Column(nullable = false)
    @Value("${gamesession.default.allowUsersToAdd}")
    private boolean allowUsersToAdd;

    @Column(nullable = false)
    @Value("${gamesession.default.addLimit}")
    private int addLimit;

    @Column(nullable = false)
    @Value("${gamesession.default.selectionLimit}")
    private int selectionLimit;

    @Column(nullable = false)
    @Value("${gamesession.default.timerLength}")
    private int timerLength; //Seconds

    @Column(nullable = false, length = 19)
    private String title; //GameTitle

    @Column(nullable = false)
    @OneToMany(targetEntity = UserGameSessionInfo.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "gameSession")
    @Fetch(org.hibernate.annotations.FetchMode.SELECT)
    private List<UserGameSessionInfo> userGameSessionInfos = new ArrayList<>();

    @Column
    private String image = "default-session.png";

    public GameSession() {
    }

    public GameSession(CreateGameSessionDto gameSessionDto, User user){
        this.title = gameSessionDto.getTitle();
        //TODO organisator
        this.isOrganisatorPlaying = gameSessionDto.isOrganisatorPlaying();
        this.allowUsersToAdd = gameSessionDto.isAllowUsersToAdd();
        this.addLimit = gameSessionDto.getLimit();
        this.selectionLimit = gameSessionDto.getSelectionLimit();
        this.timerLength = gameSessionDto.getTimer();

        //TODO notificatons
        List<Notification> notifications = new ArrayList<>();
        notifications.add(Notification.StartGame);
        notifications.add(Notification.EndGame);
        notifications.add(Notification.YourTurn);
        notifications.add(Notification.EndTurn);

        //Role
        GameSessionRole role;
        role = isOrganisatorPlaying ? GameSessionRole.ModeratorParticipant : GameSessionRole.Moderator;
        UserGameSessionInfo userGameSessionInfo = new UserGameSessionInfo(notifications, false, role, user, this);

        this.userGameSessionInfos.add(userGameSessionInfo);
    }


    public boolean isOrganisatorPlaying() {
        return isOrganisatorPlaying;
    }

    public void setOrganisatorPlaying(boolean organisatorPlaying) {
        isOrganisatorPlaying = organisatorPlaying;
    }

    public boolean isAllowUsersToAdd() {
        return allowUsersToAdd;
    }

    public void setAllowUsersToAdd(boolean allowUsersToAdd) {
        this.allowUsersToAdd = allowUsersToAdd;
    }

    public int getAddLimit() {
        return addLimit;
    }

    public void setAddLimit(int addLimit) {
        this.addLimit = addLimit;
    }

    public int getSelectionLimit() {
        return selectionLimit;
    }

    public void setSelectionLimit(int selectionLimit) {
        this.selectionLimit = selectionLimit;
    }

    public int getTimerLength() {
        return timerLength;
    }

    public void setTimerLength(int timerLength) {
        this.timerLength = timerLength;
    }

    public Long getGameSessionId() {
        return gameSessionId;
    }

    public void setGameSessionId(Long gameSessionId) {
        this.gameSessionId = gameSessionId;
    }

    public List<UserGameSessionInfo> getUserGameSessionInfos() {
        return userGameSessionInfos;
    }

    public void setUserGameSessionInfos(List<UserGameSessionInfo> userGameSessionInfos) {
        this.userGameSessionInfos = userGameSessionInfos;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOrganisatorName(){
        for(UserGameSessionInfo info : userGameSessionInfos){
            if (info.getRole() == GameSessionRole.ModeratorParticipant || info.getRole() == GameSessionRole.Moderator)
                return info.getUser().getUsername();
        }
        return "";
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
