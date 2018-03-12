package be.kdg.kandoe.domain;

import java.time.LocalDateTime;

public class Message {
    private long id;
    private String from;
    private String content;
    private GameSession session;
    private LocalDateTime DateTime;

    public Message() {
    }

    public Message(long id, String from, String content, GameSession session, LocalDateTime dateTime) {
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
