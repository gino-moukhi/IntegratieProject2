package be.kdg.kandoe.controller.rest;

import be.kdg.kandoe.service.declaration.EmailService;
import be.kdg.kandoe.service.implementation.EmailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MailRestController {
    private JavaMailSender sender;
    private EmailService emailService;

    @Autowired
    public MailRestController(@Qualifier("getJavaMailSender") JavaMailSender sender) {
        this.sender = sender;
        emailService = new EmailServiceImpl(sender);
    }

    @RequestMapping("api/public/mail")
    @ResponseBody
    public String sendMail() {
        try {
            String target = "moukhig@gmail.com";
            //String target = "gino.moukhi@student.kdg.be";
            String subject = "Hello there";
            String body = "This was a message from general Kenobi";
            emailService.sendSimpleMessage(target,subject,body);
            return "Mail sent";
        } catch (Exception e) {
            return "Error while sending mail";
        }
    }
}
