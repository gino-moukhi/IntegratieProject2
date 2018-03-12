package be.kdg.kandoe.service.declaration;

import be.kdg.kandoe.domain.GameSession;
import be.kdg.kandoe.domain.Notification;
import be.kdg.kandoe.service.exception.EmailServiceException;
import org.springframework.mail.SimpleMailMessage;

import javax.mail.SendFailedException;

public interface EmailService {
    SimpleMailMessage sendSimpleMail(String to, String subject, String body) throws EmailServiceException;
    SimpleMailMessage sendNotificationMail(String to, String sessionTitle, Notification notification) throws EmailServiceException;
}
