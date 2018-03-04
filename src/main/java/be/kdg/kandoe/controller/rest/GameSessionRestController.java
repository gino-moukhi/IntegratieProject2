package be.kdg.kandoe.controller.rest;

import be.kdg.kandoe.domain.GameSession;
import be.kdg.kandoe.domain.Notification;
import be.kdg.kandoe.domain.UserGameSessionInfo;
import be.kdg.kandoe.domain.user.User;
import be.kdg.kandoe.dto.RequestUserDto;
import be.kdg.kandoe.dto.gameSession.CreateGameSessionDto;
import be.kdg.kandoe.dto.gameSession.NotificationDto;
import be.kdg.kandoe.service.declaration.AuthenticationHelperService;
import be.kdg.kandoe.service.declaration.GameSessionService;
import be.kdg.kandoe.service.declaration.StorageService;
import be.kdg.kandoe.service.declaration.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@RestController
public class GameSessionRestController {

    private final UserService userService;

    private final GameSessionService gameSessionService;

    private final StorageService storageService;

    private final AuthenticationHelperService authenticationHelperService;

    @Autowired
    public GameSessionRestController(UserService userService, GameSessionService gameSessionService, StorageService storageService, AuthenticationHelperService authenticationHelperService) {
        this.userService = userService;
        this.gameSessionService = gameSessionService;
        this.storageService = storageService;
        this.authenticationHelperService = authenticationHelperService;
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
            CreateGameSessionDto dto = new CreateGameSessionDto(gameSession.getTitle(), gameSession.getOrganisatorName(), gameSession.isOrganisatorPlaying(), gameSession.isAllowUsersToAdd(), gameSession.getAddLimit(), gameSession.getSelectionLimit(), gameSession.getTimerLength());
            dto.setGameSessionId(gameSession.getGameSessionId());
            returnGameSessionDtos.add(dto);
        }

        return returnGameSessionDtos;
    }


    //GET ALL SESSIONS A USER PARTICIPATES IN
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
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity createGameSession(@RequestBody CreateGameSessionDto createGameSessionDto){
        GameSession gameSession = new GameSession(createGameSessionDto, userService.findUserByUsername(createGameSessionDto.getOrganisator()));
        GameSession savedGameSession = gameSessionService.addGameSession(gameSession);
        return ResponseEntity.ok(savedGameSession.getGameSessionId());
    }


    //Update notification settings from a session
    @PostMapping("/api/private/users/{username}/sessions/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity updateNotificationSettingsOfUser(@PathVariable String username, @PathVariable Long id, @RequestBody NotificationDto notificationDto, HttpServletRequest request){
//        String usernameFromToken = (String) request.getAttribute("username");
//        User tokenUser = userService.findUserByUsername(usernameFromToken);
//        boolean isAdmin = false;
//        User requestUser = userService.findUserByUsername(username);
//
//
//        for(GrantedAuthority authority : tokenUser.getAuthorities()){
//            if(authority.getAuthority().equalsIgnoreCase("ROLE_ADMIN")){
//                isAdmin = true;
//            }
//        }
//
//        if(!isAdmin && !tokenUser.getUsername().equalsIgnoreCase(username)){
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }

        User requestUser = userService.findUserByUsername(username);

        if (!authenticationHelperService.userIsAllowedToAccessResource(request, username)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        };

        if(requestUser == null){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        //todo code verplaatsen
        List<Notification> notifications = new ArrayList<>();

        if (notificationDto.startGame) notifications.add(Notification.StartGame);
        if (notificationDto.endGame) notifications.add(Notification.EndGame);
        if (notificationDto.yourTurn) notifications.add(Notification.YourTurn);
        if (notificationDto.endTurn) notifications.add(Notification.EndTurn);

        GameSession gameSession = gameSessionService.getGameSessionWithId(id);
        List<UserGameSessionInfo> userGameSessionInfos = gameSession.getUserGameSessionInfos();

        for(UserGameSessionInfo gameSessionInfo : userGameSessionInfos){
            if(gameSessionInfo.getUser().getUsername().equalsIgnoreCase(username)){
                gameSessionInfo.setNotifications(notifications);
            }
        }

        GameSession u = gameSessionService.updateGameSession(gameSession);

        return ResponseEntity.ok(notificationDto);

    }


    //Get notification settings from a session that the user participates in
    @GetMapping("/api/private/users/{username}/sessions/{id}/notifications")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<NotificationDto> getNotificationsOfUserFromGameSession(@PathVariable String username, @PathVariable Long id, HttpServletRequest request){
//        String usernameFromToken = (String) request.getAttribute("username");
//        User tokenUser = userService.findUserByUsername(usernameFromToken);
//        boolean isAdmin = false;
//        User requestUser = userService.findUserByUsername(username);
//
//
//        for(GrantedAuthority authority : tokenUser.getAuthorities()){
//            if(authority.getAuthority().equalsIgnoreCase("ROLE_ADMIN")){
//                isAdmin = true;
//            }
//        }
//
//        if(!isAdmin && !tokenUser.getUsername().equalsIgnoreCase(username)){
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }

        User requestUser = userService.findUserByUsername(username);

        if (!authenticationHelperService.userIsAllowedToAccessResource(request, username)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        };

        if(requestUser == null){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        GameSession gameSession = gameSessionService.getGameSessionWithId(id);

        for(UserGameSessionInfo gameSessionInfo : gameSession.getUserGameSessionInfos()){
            if(gameSessionInfo.getUser().getUsername().equalsIgnoreCase(username)){
                NotificationDto notificationDto = new NotificationDto(gameSessionInfo.getNotifications());
                return ResponseEntity.ok(notificationDto);
            }
        }

        return ResponseEntity.notFound().build();
    }


    //GET ALL USERS FROM SESSION
    @GetMapping("/api/private/sessions/{id}/users")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity getUsersFromSession(@PathVariable Long id, HttpServletRequest request){
        GameSession gameSession = gameSessionService.getGameSessionWithId(id);

        if(gameSession == null){
            return ResponseEntity.notFound().build();
        }

        List<RequestUserDto> userDtos = new ArrayList<>();
        for(UserGameSessionInfo gameSessionInfo : gameSession.getUserGameSessionInfos()){
            User user = gameSessionInfo.getUser();
            userDtos.add(new RequestUserDto(user.getUsername(), user.getFirstName(), user.getLastName(), user.getEmail(), gameSessionInfo.getRole().name()));
        }

        return ResponseEntity.ok(userDtos);
    }
}
