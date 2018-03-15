package be.kdg.kandoe.service.declaration;

import be.kdg.kandoe.domain.GameSession;
import be.kdg.kandoe.domain.Notification;
import be.kdg.kandoe.service.exception.EmailServiceException;
import org.springframework.mail.SimpleMailMessage;

import javax.mail.SendFailedException;
import javax.mail.internet.MimeMessage;

public interface EmailService {
    SimpleMailMessage sendSimpleMail(String to, String subject, String body) throws EmailServiceException;
    SimpleMailMessage sendNotificationMail(String to, String sessionTitle, Notification notification) throws EmailServiceException;
    MimeMessage sendVerificationMail(String email, String destinationUrlWithToken);
}
