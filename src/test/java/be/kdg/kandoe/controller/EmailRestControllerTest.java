package be.kdg.kandoe.controller;

import be.kdg.kandoe.domain.GameSession;
import be.kdg.kandoe.domain.Notification;
import be.kdg.kandoe.domain.user.Gender;
import be.kdg.kandoe.domain.user.User;
import be.kdg.kandoe.dto.UserDto;
import be.kdg.kandoe.dto.gameSession.CreateGameSessionDto;
import be.kdg.kandoe.service.declaration.EmailService;
import be.kdg.kandoe.service.implementation.EmailServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Calendar;
import java.util.Locale;

import static org.hamcrest.core.IsEqual.equalTo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class EmailRestControllerTest {
    private EmailService emailService;
    private JavaMailSender javaMailSender;
    private static TestRestTemplate restTemplate;
    static String callURL = "http://localhost:9090/";

    private User user;
    private CreateGameSessionDto createGameSessionDto;
    private GameSession gameSession;

    @Before
    public void setUp() throws Exception {
        javaMailSender = new JavaMailSenderImpl();
        emailService = new EmailServiceImpl(javaMailSender);
        restTemplate = new TestRestTemplate();


        createGameSessionDto = new CreateGameSessionDto("BEER", "L0newolf", true, false, 5, 2, 10000);
        user = new User(new UserDto("L0newolf", "Snor123", "Gino", "Moukhi", "moukhig@gmail.com", 20, Gender.Male, Calendar.getInstance(Locale.ENGLISH)));


        gameSession = new GameSession(createGameSessionDto, user);
        gameSession.setGameSessionId((long) 1);
        gameSession.getUserGameSessionInfos().get(0).setUserGameSessionInfoId((long) 1);
    }

    @Test
    public void testNotificationMail() throws Exception {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("moukhig@gmail.com");
        message.setSubject("insert subject here");
        message.setText("body");
        ResponseEntity<SimpleMailMessage> response = restTemplate.exchange(callURL + "api/public/notificationMail/"
                + message.getTo() + "/" + gameSession.getGameSessionId() + "/" + Notification.StartGame, HttpMethod.POST,null,SimpleMailMessage.class);
        Assert.assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
    }
}
