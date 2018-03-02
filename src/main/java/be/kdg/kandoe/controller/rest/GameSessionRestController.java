package be.kdg.kandoe.controller.rest;

import be.kdg.kandoe.domain.GameSession;
import be.kdg.kandoe.domain.user.User;
import be.kdg.kandoe.dto.gameSession.CreateGameSessionDto;
import be.kdg.kandoe.service.declaration.GameSessionService;
import be.kdg.kandoe.service.declaration.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@RestController
public class GameSessionRestController {

    private final UserService userService;

    private final GameSessionService gameSessionService;


    @Autowired
    public GameSessionRestController(UserService userService, GameSessionService gameSessionService) {
        this.userService = userService;
        this.gameSessionService = gameSessionService;
    }


    //GET ALL SESSIONS
    @GetMapping("/api/private/gamesessions")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<GameSession> getGameSessions(){
        return gameSessionService.getAllGameSessions();
    }

    //TODO Remove once repo query is fixed
    @GetMapping("/api/private/temp/gamesessions")
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<CreateGameSessionDto> getGameSessionsTemp(){
        List<GameSession> gameSessions = gameSessionService.getAllGameSessions();
        List<CreateGameSessionDto> returnGameSessionDtos = new ArrayList<>();

        for(GameSession gameSession : gameSessions){
            returnGameSessionDtos.add(new CreateGameSessionDto(gameSession.getTitle(), gameSession.getOrganisatorName(), gameSession.isOrganisatorPlaying(), gameSession.isAllowUsersToAdd(), gameSession.getAddLimit(), gameSession.getSelectionLimit(), gameSession.getTimerLength()));
        }

        return returnGameSessionDtos;
    }


    //GET ALL SESSIONS
    @GetMapping("/api/private/{username}/gamesessions")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity getUserGameSessions(@RequestParam String username, HttpServletRequest request){

        String usernameFromToken = (String) request.getAttribute("username");
        User tokenUser = userService.findUserByUsername(usernameFromToken);
        boolean isAdmin = false;
        User requestUser = userService.findUserByUsername(username);


        for(GrantedAuthority authority : tokenUser.getAuthorities()){
            if(authority.getAuthority().equalsIgnoreCase("ROLE_ADMIN")){
                isAdmin = true;
            }
        }

        if(!isAdmin && !tokenUser.getUsername().equalsIgnoreCase(username)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<GameSession> gameSessions = gameSessionService.getUserGameSessions(username);

        return null;
    }

    //CREATE SESSION
    @PostMapping("/api/private/sessions")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity createGameSession(@RequestBody CreateGameSessionDto createGameSessionDto){
        GameSession gameSession = new GameSession(createGameSessionDto, userService.findUserByUsername(createGameSessionDto.getOrganisator()));
        GameSession savedGameSession = gameSessionService.addGameSession(gameSession);
        return ResponseEntity.ok().build();
    }

}
