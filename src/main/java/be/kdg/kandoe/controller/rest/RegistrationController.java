package be.kdg.kandoe.controller.rest;

import be.kdg.kandoe.domain.user.User;
import be.kdg.kandoe.dto.UserDto;
import be.kdg.kandoe.service.declaration.UserService;
import be.kdg.kandoe.service.exception.UserServiceException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
public class RegistrationController {
    private final Logger logger = Logger.getLogger(UserRestController.class);
    private UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/api/public/login")
    public ResponseEntity login(@RequestBody UserDto userDto){
        if(!userService.checkLogin(userDto.getUsername(), userDto.getPassword())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Credentials are wrong!");
        }
//        try{
//            userService.checkLogin(userDto.getUsername(), userDto.getPassword());
//        }catch (UserServiceException e){
//            return ResponseEntity.status(HttpStatus.CONFLICT).body("Credentials are wrong!");
//        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/api/public/register")
    public ResponseEntity register(@RequestBody UserDto userDto){
        String errorMessage = "";
        boolean usernameGood = userService.checkUsernameCredentials(userDto.getUsername());
        boolean emailGood = userService.checkEmailCredentials(userDto.getEmail());

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
            userService.addUser(user);
//            return ResponseEntity.status(HttpStatus.CREATED).body("User has been registered!");
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
    }

    @GetMapping("/api/private/loggedin")
    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_ADMIN')")
    public ResponseEntity testLoggedIn(@AuthenticationPrincipal User user){
        return ResponseEntity.ok().build();
    }
}
