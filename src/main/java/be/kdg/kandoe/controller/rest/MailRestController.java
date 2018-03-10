package be.kdg.kandoe.controller.rest;

import be.kdg.kandoe.domain.GameSession;
import be.kdg.kandoe.domain.Notification;
import be.kdg.kandoe.domain.user.Gender;
import be.kdg.kandoe.domain.user.User;
import be.kdg.kandoe.dto.UserDto;
import be.kdg.kandoe.dto.gameSession.CreateGameSessionDto;
import be.kdg.kandoe.service.declaration.EmailService;
import be.kdg.kandoe.service.implementation.EmailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Calendar;
import java.util.Locale;

@Controller
public class MailRestController {
    private JavaMailSender sender;
    private EmailService emailService;

    @Autowired
    public MailRestController(@Qualifier("getJavaMailSender") JavaMailSender sender) {
        this.sender = sender;
        emailService = new EmailServiceImpl(sender);
    }

    @RequestMapping("api/public/mail")
    @ResponseBody
    public String sendSimpleMail() {
        try {
            String target = "snor123snor123@gmail.com";
            String subject = "Hello there";
            String body = "UNLIMITED POWER!";
            emailService.sendSimpleMail(target, subject, body);
            return "Mail sent to " + target + " with body: " + body;
        } catch (Exception e) {
            return "Error while sending mail";
        }
    }

    @RequestMapping("api/public/notificationMail")
    @ResponseBody
    public String sendNotificationMail() {
        try {
            CreateGameSessionDto createGameSessionDto = new CreateGameSessionDto("BEER", "Team CPP", true, false, 5, 2, 10000);
            User user = new User(new UserDto("L0newolf", "Snor123", "Gino", "Moukhi", "moukhig@gmail.com", 20, Gender.Male, Calendar.getInstance(Locale.ENGLISH)));
            GameSession session = new GameSession(createGameSessionDto, user);

            emailService.sendNotificationMail(user.getEmail(), session.getTitle(), Notification.StartGame);
            return user.getEmail() + " " + session + " " + Notification.StartGame;
        } catch (Exception e) {
            return "Error while sending the message";
        }
    }
}
