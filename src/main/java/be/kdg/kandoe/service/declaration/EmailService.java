package be.kdg.kandoe.service.declaration;

import be.kdg.kandoe.domain.GameSession;
import be.kdg.kandoe.domain.Notification;

import javax.mail.SendFailedException;

public interface EmailService {
    void sendSimpleMail(String to, String subject, String body) throws SendFailedException;
    //void sendSimpleMessageToMultipleUsers(String[] to, String subject, String body) throws SendFailedException;
    void sendNotificationMail(String to, String sessionTitle, Notification notification) throws SendFailedException;
}
