package be.kdg.kandoe.dto;

import be.kdg.kandoe.dto.gameSession.GameSessionDto;
import be.kdg.kandoe.dto.theme.CardDto;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class VoteDto {
    private long voteId;
    private GameSessionDto gameSession;
    private CardDto card;
    private UserDto userDto;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "GMT")
    private LocalDateTime time;

    public VoteDto() {
    }

    public VoteDto(long voteId, GameSessionDto gameSession, CardDto card, UserDto userDto, LocalDateTime time) {
        this.voteId = voteId;
        this.gameSession = gameSession;
        this.card = card;
        this.userDto = userDto;
        this.time = time;
    }

    public long getVoteId() {
        return voteId;
    }

    public void setVoteId(long voteId) {
        this.voteId = voteId;
    }

    public GameSessionDto getGameSession() {
        return gameSession;
    }

    public void setGameSession(GameSessionDto gameSession) {
        this.gameSession = gameSession;
    }

    public CardDto getCard() {
        return card;
    }

    public void setCard(CardDto card) {
        this.card = card;
    }

    public UserDto getUserDto() {
        return userDto;
    }

    public void setUserDto(UserDto userDto) {
        this.userDto = userDto;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}