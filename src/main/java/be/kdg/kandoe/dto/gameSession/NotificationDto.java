package be.kdg.kandoe.dto.gameSession;

import be.kdg.kandoe.domain.Notification;

import java.util.List;

public class NotificationDto {
    public boolean startGame = false;
    public boolean endGame = false;
    public boolean yourTurn = false;
    public boolean endTurn = false;

    public NotificationDto() {
    }

    public NotificationDto(boolean startGame, boolean endGame, boolean yourTurn, boolean endTurn) {
        this.startGame = startGame;
        this.endGame = endGame;
        this.yourTurn = yourTurn;
        this.endTurn = endTurn;
    }

    public NotificationDto(List<Notification> notifications){
        for(Notification notification : notifications){
            switch (notification){
                case StartGame: this.startGame = true;break;
                case EndGame: this.endGame= true;break;
                case YourTurn: this.yourTurn = true;break;
                case EndTurn: this.endTurn = true;break;
            }
        }
    }

    public boolean isStartGame() {
        return startGame;
    }

    public void setStartGame(boolean startGame) {
        this.startGame = startGame;
    }

    public boolean isEndGame() {
        return endGame;
    }

    public void setEndGame(boolean endGame) {
        this.endGame = endGame;
    }

    public boolean isYourTurn() {
        return yourTurn;
    }

    public void setYourTurn(boolean yourTurn) {
        this.yourTurn = yourTurn;
    }

    public boolean isEndTurn() {
        return endTurn;
    }

    public void setEndTurn(boolean endTurn) {
        this.endTurn = endTurn;
    }
}
