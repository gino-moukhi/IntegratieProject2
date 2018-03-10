package be.kdg.kandoe.service.declaration;

import javax.mail.SendFailedException;

public interface EmailService {
    void sendSimpleMessage(String to, String subject, String body) throws SendFailedException;
}
