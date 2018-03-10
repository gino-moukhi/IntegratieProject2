package be.kdg.kandoe.service;

import be.kdg.kandoe.KandoeApplication;
import be.kdg.kandoe.service.implementation.EmailServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit4.SpringRunner;
import org.mockito.Mockito.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = KandoeApplication.class)
public class EmailServiceImplTest {
    private EmailServiceImpl mailService;
    private JavaMailSender javaMailSender;

    @Before
    public void setup(){
        javaMailSender = mock(JavaMailSender.class);
        mailService = new EmailServiceImpl(javaMailSender);
    }

    @Test
    public void sendSimpleMessage() throws Exception {
        String target = "gino.moukhi@student.kdg.be";
        String subject = "Hello there";
        String body = "This was a message from general Kenobi";
        mailService.sendSimpleMessage(target,subject,body);
    }

}