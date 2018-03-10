package be.kdg.kandoe.service.implementation;

import be.kdg.kandoe.domain.GameSession;
import be.kdg.kandoe.domain.Notification;
import be.kdg.kandoe.service.declaration.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.mail.SendFailedException;

@Component
public class EmailServiceImpl implements EmailService {

    private JavaMailSender emailSender;

    @Autowired
    public EmailServiceImpl(@Qualifier("getJavaMailSender") JavaMailSender javaMailSender) {
        this.emailSender = javaMailSender;
    }

    @Override
    public void sendSimpleMail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        emailSender.send(message);
    }

/*    @Override
    public void sendSimpleMessageToMultipleUsers(String[] to, String subject, String body) throws SendFailedException {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        emailSender.send(message);
    }*/

    @Override
    public void sendNotificationMail(String to, String sessionTitle, Notification notification) throws SendFailedException {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(sessionTitle +": "+notification);
        message.setText(notification.getMessage());
        emailSender.send(message);
    }
}
