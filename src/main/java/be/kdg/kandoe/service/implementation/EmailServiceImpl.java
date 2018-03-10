package be.kdg.kandoe.service.implementation;

import be.kdg.kandoe.service.declaration.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailServiceImpl implements EmailService {

    private JavaMailSender emailSender;

    @Autowired
    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.emailSender = javaMailSender;
    }

    @Override
    public void sendSimpleMessage(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        emailSender.send(message);
    }
}
