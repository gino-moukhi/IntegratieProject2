package be.kdg.kandoe.controller.rest;

import be.kdg.kandoe.domain.user.User;
import be.kdg.kandoe.dto.UserDto;
import be.kdg.kandoe.service.declaration.UserService;
import be.kdg.kandoe.service.exception.UserServiceException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Calendar;
import java.util.Date;

@RestController
public class AuthenticationController {
    private final Logger logger = Logger.getLogger(UserRestController.class);
    private UserService userService;

    @Value("${jwt.expiration_time}")
    private long EXPIRATION_TIME;

    @Value("${jwt.secret}")
    private String SECRET;

    @Autowired
    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/api/public/login")
    public ResponseEntity login(@RequestBody UserDto userDto){
        if(!userService.checkLogin(userDto.getUsername(), userDto.getPassword())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Credentials are wrong!");
        }
        User user = userService.findUserByUsername(userDto.getUsername());
        StringBuilder roles = new StringBuilder();
        user.getRoles().forEach(role -> roles.append(role.getRoleType() + " "));

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, (int)EXPIRATION_TIME);

        String jwtToken = Jwts.builder()
                            .setSubject(userDto.getUsername())
                            .claim("roles", roles.toString())
                            .setIssuedAt(new Date())
                            .setExpiration(calendar.getTime())
                            .signWith(SignatureAlgorithm.HS256, SECRET.getBytes())
                            .compact();
//        return ResponseEntity.ok(jwtToken);
        return new ResponseEntity<>(jwtToken, HttpStatus.OK);
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
    public ResponseEntity testLoggedIn(@AuthenticationPrincipal User user){
        return ResponseEntity.ok().body("You are logged in!");
    }
}
