package be.kdg.kandoe.controller.rest;

import be.kdg.kandoe.domain.user.User;
import be.kdg.kandoe.dto.UserDto;
import be.kdg.kandoe.service.declaration.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class RegistrationController {
    private final Logger logger = Logger.getLogger(UserRestController.class);
    private UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    //Eerste enkel van de url, tweede van overal, en 3de is de global config
    //@CrossOrigin(origins = "http://localhost:4200")
    //@CrossOrigin
    @PostMapping("/api/login")
    public ResponseEntity login(@RequestBody UserDto userDto){
        logger.debug(userDto);
        return ResponseEntity.ok().build();
    }
}
