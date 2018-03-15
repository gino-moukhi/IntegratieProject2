package be.kdg.kandoe.controller;

import be.kdg.kandoe.dto.UserDto;
import be.kdg.kandoe.dto.VoteDto;
import be.kdg.kandoe.dto.converter.DtoConverter;
import be.kdg.kandoe.dto.gameSession.NotificationDto;
import be.kdg.kandoe.service.declaration.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;

public class GameController {

    public GameService gameService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @MessageMapping("/{sessionId}/start")
    @SendTo("/gamesession/{sessionId}/notifications")
    public NotificationDto startGame(@DestinationVariable long sessionId){
        return new NotificationDto(true, false, false, false);
    }

    @MessageMapping("/{sessionId}/move/{cardId}")
    @SendTo("/gamesession/{sessionId}/moves")
    public VoteDto vote(@DestinationVariable long sessionId, @DestinationVariable long cardId, UserDto user){
        //todo: User-UserDto?
        //VoteDto vote = DtoConverter.toVoteDto(gameService.MakeVote(sessionId,cardId,DtoConverter.toUser(user)));
        return null;
    }
}