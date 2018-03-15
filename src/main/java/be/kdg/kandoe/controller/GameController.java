package be.kdg.kandoe.controller;

import be.kdg.kandoe.dto.VoteDto;
import be.kdg.kandoe.dto.gameSession.NotificationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;

public class GameController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/{sessionId}/start")
    @SendTo("/gamesession/{sessionId}/notifications")
    public NotificationDto startGame(@DestinationVariable long sessionId){
        return new NotificationDto(true, false, false, false);
    }

    @MessageMapping("/{sessionId}/move/{cardId}")
    @SendTo("/gamesession/{sessionId}/moves")
    public VoteDto vote(){
        return null;
    }
}