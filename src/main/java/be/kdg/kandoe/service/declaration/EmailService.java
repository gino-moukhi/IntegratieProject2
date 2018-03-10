package be.kdg.kandoe.service.declaration;

public interface EmailService {
    void sendSimpleMessage(String to, String subject, String body);
}
