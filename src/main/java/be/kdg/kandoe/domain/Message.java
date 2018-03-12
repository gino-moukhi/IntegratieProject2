package be.kdg.kandoe.domain;

import java.time.LocalDateTime;

public class Message {
    private long id;
    private String sender;
    private String content;
    private GameSession session;
    private LocalDateTime DateTime;

    public Message() {
    }

    public Message(long id, String sender, String content, GameSession session, LocalDateTime dateTime) {
        this.id = id;
        this.sender = sender;
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

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public GameSession getSession() {
        return session;
    }

    public void setSession(GameSession session) {
        this.session = session;
    }

    public LocalDateTime getDateTime() {
        return DateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        DateTime = dateTime;
    }
}
