package be.kdg.kandoe.controller.rest;

import be.kdg.kandoe.domain.user.UserTokenState;
import be.kdg.kandoe.dto.UserDto;
import be.kdg.kandoe.security.auth.JwtAuthenticationRequest;
import be.kdg.kandoe.service.declaration.AuthenticationHelperService;
import be.kdg.kandoe.service.exception.CustomAuthenticationException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@CrossOrigin(origins = "https://angularkandoe.herokuapp.com")
public class AuthenticationController {
    private final Logger logger = Logger.getLogger(UserRestController.class);
    private final AuthenticationHelperService authenticationHelperService;

    @Autowired
    public AuthenticationController(AuthenticationHelperService authenticationHelperService) {
        this.authenticationHelperService = authenticationHelperService;
    }

    @PostMapping("/api/public/login")
    public ResponseEntity createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest, HttpServletResponse response, Device device){
        try{
            UserTokenState userTokenState = authenticationHelperService.authenticate(authenticationRequest, device);
            return ResponseEntity.ok(userTokenState);
        }catch (CustomAuthenticationException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PostMapping("/api/public/register")
    public ResponseEntity register(@RequestBody UserDto userDto){
        try{
            authenticationHelperService.register(userDto);
            return ResponseEntity.ok().build();
        }catch (CustomAuthenticationException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Something went wrong while registering. Try again later");
        }

    }

}
