package be.kdg.kandoe.service;

import be.kdg.kandoe.service.declaration.EmailService;
import be.kdg.kandoe.service.implementation.EmailServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit4.SpringRunner;

import javax.mail.SendFailedException;

import static org.mockito.Mockito.mock;

@RunWith(SpringRunner.class)
public class EmailServiceImplTest {
    private EmailService mailService;
    private JavaMailSender javaMailSender;

    @Before
    public void setup(){
        javaMailSender = mock(JavaMailSender.class);
        mailService = new EmailServiceImpl(javaMailSender);
    }

    @Test
    public void sendSimpleMessage() throws SendFailedException {
        String target = "moukhig@gmail.com";
        //String target = "gino.moukhi@student.kdg.be";
        String subject = "Hello there";
        String body = "This was a message from general Kenobi";
        mailService.sendSimpleMail(target,subject,body);
    }

}