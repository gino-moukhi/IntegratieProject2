package be.kdg.kandoe.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

public class MessageDto {
    private long id;
    private String from;
    private String content;
    private Session session;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "GMT")
    private LocalDateTime DateTime;

    public MessageDto() {
    }

    public MessageDto(long id, String from, String content, Session session, LocalDateTime dateTime) {
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

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public LocalDateTime getDateTime() {
        return DateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        DateTime = dateTime;
    }
}

class Message {
    private long id;
    private String from;
    private String content;
    private Session session;
    private LocalDateTime DateTime;

    public Message() {
    }

    public Message(long id, String from, String content, Session session, LocalDateTime dateTime) {
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

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public LocalDateTime getDateTime() {
        return DateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        DateTime = dateTime;
    }
}

@Entity
@Table("MESSAGE")
class MessageJpa {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(nullable = false)
    private long id;

    @Column(nullable = false)
    private String from;

    @Column(nullable = false)
    private String content;

    @ManyToOne(cascade = CascadeType.ALL)
    private Session session;

    @Column(nullable = false)
    private LocalDateTime DateTime;

    public MessageJpa(String from, String content, Session session, LocalDateTime dateTime) {
        this.from = from;
        this.content = content;
        this.session = session;
        DateTime = dateTime;
    }

    public MessageJpa() {
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

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
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
