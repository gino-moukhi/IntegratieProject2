package be.kdg.kandoe.controller.rest;

import be.kdg.kandoe.common.DeviceProvider;
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

import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.security.Key;
import java.util.Calendar;
import java.util.Date;

@RestController
public class AuthenticationController {
    private final Logger logger = Logger.getLogger(UserRestController.class);


    @Autowired
    TokenHelper tokenHelper;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private DeviceProvider deviceProvider;

    @PostMapping("/api/public/login")
    public ResponseEntity createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest, HttpServletResponse response, Device device){

        // Perform the security

        try{
            final Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.getUsername(),
                            authenticationRequest.getPassword()
                    )
            );

            // Inject into security context
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // token creation
            User user = (User)authentication.getPrincipal();

            StringBuilder roles = new StringBuilder();
            user.getAuthorities().forEach(role -> roles.append(role.getAuthority() + " "));


            String jws = tokenHelper.generateToken( user.getUsername(), device, roles.toString());
            int expiresIn = tokenHelper.getExpiredIn(device);

            // Return the token
            UserTokenState userTokenState = new UserTokenState(jws, expiresIn);
            return ResponseEntity.ok(userTokenState);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PostMapping("/api/public/register")
    public ResponseEntity register(@RequestBody UserDto userDto){
        String errorMessage = "";
        boolean usernameGood = userDetailsService.checkUsernameCredentials(userDto.getUsername());
        boolean emailGood = userDetailsService.checkEmailCredentials(userDto.getEmail());

        if(!usernameGood && !emailGood){
            errorMessage += "Username and email already used!";
        }
        else if(!usernameGood){
            errorMessage += "Username is already used!";
        }
        else if(!emailGood){
            errorMessage += "Email is already used!";
        }
        else{
            User user = new User(userDto);
            userDetailsService.addUser(user);
//            return ResponseEntity.status(HttpStatus.CREATED).body("User has been registered!");
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
    }

    @GetMapping("/api/private/loggedin")
    public ResponseEntity testLoggedIn(@AuthenticationPrincipal User user){
        return ResponseEntity.ok().body("You are logged in!");
    }
}
