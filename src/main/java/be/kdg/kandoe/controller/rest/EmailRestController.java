package be.kdg.kandoe.controller.rest;

import be.kdg.kandoe.domain.GameSession;
import be.kdg.kandoe.domain.Notification;
import be.kdg.kandoe.domain.exception.NotificationException;
import be.kdg.kandoe.domain.user.Gender;
import be.kdg.kandoe.domain.user.User;
import be.kdg.kandoe.dto.UserDto;
import be.kdg.kandoe.dto.gameSession.CreateGameSessionDto;
import be.kdg.kandoe.service.declaration.EmailService;
import be.kdg.kandoe.service.declaration.GameSessionService;
import be.kdg.kandoe.service.declaration.UserService;
import be.kdg.kandoe.service.exception.EmailServiceException;
import be.kdg.kandoe.service.exception.GameSessionException;
import be.kdg.kandoe.service.exception.UserServiceException;
import be.kdg.kandoe.service.implementation.EmailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.*;

@Controller
public class EmailRestController {
    private JavaMailSender sender;
    private EmailService emailService;
    private GameSessionService gameSessionService;
    private UserService userService;

    @Autowired
    public EmailRestController(@Qualifier("getJavaMailSender") JavaMailSender sender, GameSessionService gameSessionService, UserService userService) {
        this.sender = sender;
        emailService = new EmailServiceImpl(sender);
        this.gameSessionService = gameSessionService;
        this.userService = userService;
    }

    @RequestMapping("api/public/mail")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public SimpleMailMessage sendSimpleMail() {
        try {
            String target = "snor123snor123@gmail.com";
            String subject = "Hello there";
            String body = "UNLIMITED POWER!";

            return emailService.sendSimpleMail(target, subject, body);
        } catch (Exception e) {
            throw new EmailServiceException("Error while sending the mail");
        }
    }

    @RequestMapping("api/public/notificationMail/{target}/{sessionId}/{notification}")
    @ResponseBody
    // @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<SimpleMailMessage> sendNotificationMail(@PathVariable(name = "target") String target, @PathVariable(name = "sessionId") Long sessionId, @PathVariable(name = "notification") Notification notification) {
        /*
            CreateGameSessionDto createGameSessionDto = new CreateGameSessionDto("BEER", "Team CPP", true, false, 5, 2, 10000);
            User user = new User(new UserDto("L0newolf", "Snor123", "Gino", "Moukhi", "moukhig@gmail.com", 20, Gender.Male, Calendar.getInstance(Locale.ENGLISH)));
            GameSession session = new GameSession(createGameSessionDto, user);
            return emailService.sendNotificationMail(user.getEmail(), session.getTitle(), Notification.StartGame);
            */
        System.out.println("CALL RECEIVED: sendNotificationMail: TARGET " + target + " SESSIONID: " + sessionId + " NOTIFICATION: " + notification);

        //todo remove this hardcoded data when the gamesessionservice doesn't throw an error
        CreateGameSessionDto createGameSessionDto = new CreateGameSessionDto("BEER", "L0newolf", true, false, 5, 2, 10000);
        User userTest = new User(new UserDto("L0newolf", "Snor123", "Gino", "Moukhi", "moukhig@gmail.com", 20, Gender.Male, Calendar.getInstance(Locale.ENGLISH)));
        userService.addUser(userTest);

        GameSession gameSession = new GameSession(createGameSessionDto, userTest);
        gameSession.setGameSessionId((long) 1);
        gameSession.getUserGameSessionInfos().get(0).setUserGameSessionInfoId((long) 1);
        //gameSessionService.addGameSession(gameSession);

            /*GameSession gameSessionToFind = gameSessionService.getGameSessionWithId(sessionId);
            if (gameSessionToFind == null) {
                ResponseEntity.status(HttpStatus.NOT_FOUND);
                throw new GameSessionException("The gamesession you are looking for does not exist");
            }*/

        User userToFind = userService.findUserByEmail(target);
        if (userToFind == null) {
            ResponseEntity.status(HttpStatus.NOT_FOUND);
            throw new UserServiceException("User does not exist");
        }
        List<Notification> notificationList = new ArrayList<>();
        notificationList = Arrays.asList(Notification.values());
        if (!notificationList.contains(notification)) {
            ResponseEntity.status(HttpStatus.NOT_FOUND);
            throw new NotificationException("this notification does not exist in the enum");
        }

        //assert gameSessionToFind != null;
        SimpleMailMessage mess = new SimpleMailMessage();

        mess = emailService.sendNotificationMail(target, gameSession.getTitle(), notification);
        return ResponseEntity.ok(mess);

    }
}
