package be.kdg.kandoe.domain;

public enum Notification {
    StartGame("The Kandoe session has started"),
    EndGame("The Kandoe session has come to an end. Thank you for participating!"),
    YourTurn("It is your turn"),
    EndTurn("Your turn has ended");

    private String message;

    Notification(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
