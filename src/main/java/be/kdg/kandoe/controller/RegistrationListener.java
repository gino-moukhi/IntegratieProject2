package be.kdg.kandoe.controller;

import be.kdg.kandoe.domain.user.User;
//import be.kdg.kandoe.service.declaration.EmailService;
import be.kdg.kandoe.service.declaration.EmailService;
import be.kdg.kandoe.service.declaration.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent>{
    private UserService userService;
    private MessageSource messageSource;
    private EmailService emailService;

    @Autowired
    public RegistrationListener(UserService userService, MessageSource messageSource/*, EmailService emailService*/) {
        this.userService = userService;
        this.messageSource = messageSource;
        this.emailService = emailService;
    }

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(OnRegistrationCompleteEvent event) {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        //userService.createRegistrationVerificationToken(user, token);

        String confirmationUrl ="/registrationConfirm/" + token;
        //String message = messageSource.getMessage("message.regSucc", null, event.getLocale());
        String destinationUrlWithToken ="http://localhost:4200" + confirmationUrl;
        emailService.sendVerificationMail(user.getEmail(), destinationUrlWithToken);
    }
}
