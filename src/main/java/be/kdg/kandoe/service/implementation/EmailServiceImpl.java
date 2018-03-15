package be.kdg.kandoe.service.implementation;

import be.kdg.kandoe.domain.GameSession;
import be.kdg.kandoe.domain.Notification;
import be.kdg.kandoe.service.declaration.EmailService;
import be.kdg.kandoe.service.exception.EmailServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.SendFailedException;
import javax.mail.internet.MimeMessage;

@Component
public class EmailServiceImpl implements EmailService {

    private JavaMailSender emailSender;

    @Autowired
    public EmailServiceImpl(@Qualifier("getJavaMailSender") JavaMailSender javaMailSender) {
        this.emailSender = javaMailSender;
    }

    @Override
    public SimpleMailMessage sendSimpleMail(String to, String subject, String body) throws EmailServiceException {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        emailSender.send(message);
        return message;
    }

    @Override
    public SimpleMailMessage sendNotificationMail(String to, String sessionTitle, Notification notification) throws EmailServiceException {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(sessionTitle + ": " + notification);
        message.setText(notification.getMessage());
        emailSender.send(message);
        return message;
    }

    @Override
    public MimeMessage sendVerificationMail(String email, String destinationUrlWithToken) {
        MimeMessage message = emailSender.createMimeMessage();
        StringBuilder builder = new StringBuilder();
        builder.append("Please click the following link to register your account");
        builder.append("<br />");
        builder.append("<a src=\"").append(destinationUrlWithToken).append("\">");
        builder.append("Registration link</a>");
        builder.append("<br />").append("<br />");
        builder.append("Thank you for choosing Kandoe developed by team CPP");

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(email);
            helper.setSubject("Registration confirmation");
            helper.setText(builder.toString(), true);
            emailSender.send(message);
            return message;
        } catch (MessagingException e) {
            throw new EmailServiceException("The verification mail could not be created");
        }
    }
}
