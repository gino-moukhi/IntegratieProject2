package be.kdg.kandoe.controller.rest;

import be.kdg.kandoe.domain.GameSession;
import be.kdg.kandoe.domain.GameSessionRole;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "https://angularkandoe.herokuapp.com")
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

    //GET ALL SESSIONS A USER PARTICIPATES IN
    @GetMapping("/api/private/{username}/gamesessions")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity getUserGameSessions(@PathVariable String username, HttpServletRequest request){

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
        List<CreateGameSessionDto> returnGameSessionDtos = new ArrayList<>();

        for(GameSession gameSession : gameSessions){
            CreateGameSessionDto dto = new CreateGameSessionDto(gameSession.getTitle(), gameSession.getHighestAccesLevelModerator(), gameSession.isOrganisatorPlaying(), gameSession.isAllowUsersToAdd(), gameSession.getAddLimit(), gameSession.getSelectionLimit(), gameSession.getTimerLength(), gameSession.getAllSubOrganisators());
            dto.setGameSessionId(gameSession.getGameSessionId());
            returnGameSessionDtos.add(dto);
        }

        return ResponseEntity.ok(returnGameSessionDtos);
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

    @PostMapping("/api/private/sessions/{id}/users/{username}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity addUserToGameSession(@PathVariable Long id, @PathVariable String username, HttpServletRequest request){
        GameSession gameSession = gameSessionService.getGameSessionWithId(id);

        if(gameSession == null ){
            ResponseEntity.status(HttpStatus.NO_CONTENT);
        }

        String organisatorName = gameSession.getHighestAccesLevelModerator();

        if(!authenticationHelperService.userIsAllowedToAccessResource(request, organisatorName)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User userToAddToSession = userService.findUserByUsername(username);

        if(userToAddToSession == null ){
            ResponseEntity.status(HttpStatus.NO_CONTENT);
        }

        GameSession savedGameSession = gameSessionService.addUserToGameSession(gameSession.getGameSessionId(), userToAddToSession);

        return ResponseEntity.ok(savedGameSession.getGameSessionId());
    }


    @PostMapping("/api/private/sessions/{id}/users/{username}/upgradeacceslevel")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity upgradeGameSessionRoleOfAUser(@PathVariable Long id, @PathVariable String username, HttpServletRequest request){
        GameSession gameSession = gameSessionService.getGameSessionWithId(id);


        //GameSessionId bestaat die?
        //Username in de token --> Is deze user een Moderator, ModeratorParticipant of Submoderator anders unauthorized
        //Username (vd user om rechten te geven) bestaat die en is die nog geen Moderator, ModeratorParticipant of Submoderator

        if(gameSession == null ){
            ResponseEntity.status(HttpStatus.NO_CONTENT).body("Gamesession was not found!");
        }

        String tokenUsername = (String) request.getAttribute("username");

        //Role of the user from the token
        GameSessionRole role = gameSession.getRoleOfUser(tokenUsername);

        if(role == null){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("User that made the request is not part of this game session!");
        }

        if(role == GameSessionRole.Participant){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User doesn't have the right permissions to grant a higher acces level to a user");
        }

        //Role of the user that needs to be granted a higher acces level
        GameSessionRole gameSessionRole = gameSession.getRoleOfUser(username);

        if(gameSessionRole == null){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("The user that needs to be granted a higher acces level is not part of the game session");
        }

        if(gameSessionRole != GameSessionRole.Participant){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User is already a moderator!");
        }

        gameSession.setRoleOfUser(username, GameSessionRole.SubModerator);

        gameSessionService.updateGameSession(gameSession);

        return ResponseEntity.ok().build();
    }

}
