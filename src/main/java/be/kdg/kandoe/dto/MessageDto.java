package be.kdg.kandoe.dto;

import be.kdg.kandoe.dto.gameSession.CreateGameSessionDto;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

public class MessageDto {
    private long id;
    private String from;
    private String content;
    private CreateGameSessionDto session;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "GMT")
    private LocalDateTime DateTime;

    public MessageDto() {
    }

    public MessageDto(long id, String from, String content, CreateGameSessionDto session, LocalDateTime dateTime) {
        this.id = id;
        this.from = from;
        this.content = content;
        this.session = session;
        DateTime = dateTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public CreateGameSessionDto getSession() {
        return session;
    }

    public void setSession(CreateGameSessionDto session) {
        this.session = session;
    }

    public LocalDateTime getDateTime() {
        return DateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        DateTime = dateTime;
    }
}

class Converter{
    public static Message messageConverter(MessageDto dto){
       return new Message(dto.getId(),dto.getFrom(),dto.getContent(), dto.getSession(), dto.getDateTime());
    }

    public static MessageDto messageDtoConverter(Message origin){
        return new MessageDto(origin.getId(), origin.getFrom(), origin.getContent(), origin.getSession(), origin.getDateTime());
    }

    public static Message messageConverter(MessageJpa jpa){
        return new Message(jpa.getId(),jpa.getFrom(),jpa.getContent(), jpa.getSession(), jpa.getDateTime());
    }

    public static MessageJpa messageJpaConverter(Message origin){
        MessageJpa result =  new MessageJpa(origin.getFrom(), origin.getContent(), origin.getSession(), origin.getDateTime());
        result.setId(origin.getId());
        return result;
    }
}
