package be.kdg.kandoe.controller.rest;

import be.kdg.kandoe.common.DeviceProvider;
import be.kdg.kandoe.controller.OnRegistrationCompleteEvent;
import be.kdg.kandoe.domain.RegistrationVerificationToken;
import be.kdg.kandoe.domain.user.User;
import be.kdg.kandoe.domain.user.UserTokenState;
import be.kdg.kandoe.dto.UserDto;
import be.kdg.kandoe.security.TokenHelper;
import be.kdg.kandoe.security.auth.JwtAuthenticationRequest;
import be.kdg.kandoe.service.declaration.UserService;
import be.kdg.kandoe.service.exception.UserServiceException;
import be.kdg.kandoe.service.implementation.CustomUserDetailsService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.security.Key;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

@RestController
public class AuthenticationController {
    private final Logger logger = Logger.getLogger(UserRestController.class);
    private final TokenHelper tokenHelper;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final DeviceProvider deviceProvider;
    private final ApplicationEventPublisher eventPublisher;
    private final UserService userService;
    private final MessageSource messageSource;

    @Autowired
    public AuthenticationController(TokenHelper tokenHelper, AuthenticationManager authenticationManager, CustomUserDetailsService userDetailsService, DeviceProvider deviceProvider, ApplicationEventPublisher eventPublisher, UserService userService, MessageSource messageSource) {
        this.tokenHelper = tokenHelper;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.deviceProvider = deviceProvider;
        this.eventPublisher = eventPublisher;
        this.userService = userService;
        this.messageSource = messageSource;
    }

    @PostMapping("/api/public/login")
    public ResponseEntity createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest, HttpServletResponse response, Device device) {

        // Perform the security

        try {
            final Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.getUsername(),
                            authenticationRequest.getPassword()
                    )
            );

            // Inject into security context
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // token creation
            User user = (User) authentication.getPrincipal();

            StringBuilder roles = new StringBuilder();
            user.getAuthorities().forEach(role -> roles.append(role.getAuthority() + " "));


            String jws = tokenHelper.generateToken(user.getUsername(), device, roles.toString());
            int expiresIn = tokenHelper.getExpiredIn(device);

            // Return the token
            UserTokenState userTokenState = new UserTokenState(jws, expiresIn);
            return ResponseEntity.ok(userTokenState);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PostMapping("/api/public/register")
    public ResponseEntity register(@RequestBody UserDto userDto, WebRequest request) {
        String errorMessage = "";
        boolean usernameGood = userDetailsService.checkUsernameCredentials(userDto.getUsername());
        boolean emailGood = userDetailsService.checkEmailCredentials(userDto.getEmail());

        if (!usernameGood && !emailGood) {
            errorMessage += "Username and email already used!";
        } else if (!usernameGood) {
            errorMessage += "Username is already used!";
        } else if (!emailGood) {
            errorMessage += "Email is already used!";
        } else {
            User user = new User(userDto);
            userDetailsService.addUser(user);
            try {
                String appUrl = "/api/public/register";
                eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user, request.getLocale(), appUrl));
            } catch (Exception e) {
                e.printStackTrace();
            }
//            return ResponseEntity.status(HttpStatus.CREATED).body("User has been registered!");
            //todo remove these 2 lines and use the confirmRegistration method down below
            //user.setRegistered(true);
            //userService.saveUser(user);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
    }

    @GetMapping("/api/private/loggedin")
    public ResponseEntity testLoggedIn(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok().body("You are logged in!");
    }

    /*
    @GetMapping("/api/public/regitrationConfirm")
    public String confirmRegistration(WebRequest request, @RequestParam("token") String token) {
        Locale locale = request.getLocale();
        RegistrationVerificationToken verificationToken = userService.getRegistrationVerificationToken(token);
        if (verificationToken == null) {
            String message = messageSource.getMessage("auth.message.invalidToken", null, locale);
            return "redirect:/badUser.html?lang=" + locale.getLanguage();
        }

        User user = verificationToken.getUser();
        Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            String messageValue = messageSource.getMessage("auth.message.expired", null, locale);
            return "redirect:/badUser.html?lang=" + locale.getLanguage();
        }

        user.setRegistered(true);
        userService.saveUser(user);
        return "redirect:/login.html?lang=" + request.getLocale().getLanguage();
    }
    */
}
