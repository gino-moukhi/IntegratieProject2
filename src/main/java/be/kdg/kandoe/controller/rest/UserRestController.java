package be.kdg.kandoe.controller.rest;

import be.kdg.kandoe.domain.user.User;
import be.kdg.kandoe.service.declaration.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class UserRestController {
    private final Logger logger = Logger.getLogger(UserRestController.class);
    private final UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    //GET ALL USERS
    @GetMapping("/api/private/users")
    public List<User> getUsers(){
        return userService.findUsers();
    }

    //GET ONE USER
    @GetMapping("/api/private/users/{userId}")
    public ResponseEntity<User> getUser(@PathVariable long userId){
        User user = userService.findUserById(userId);
        if(user == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(user);
    }

    //CREATE USER
    @PostMapping("/api/private/users")
    public User createUser(@Valid @RequestBody User user){
        return userService.addUser(user);
    }

    //UPDATE
    @PostMapping("/api/private/users/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable Long userId, @Valid @RequestBody User changedUser){
        User user = userService.findUserById(userId);

        if(user == null){
            return ResponseEntity.notFound().build();
        }

        //Todo change user details to new details

        User updatedUser = userService.saveUser(user);
        return ResponseEntity.ok(updatedUser);
    }

    //DELETE
    @DeleteMapping("api/private/users/{userId}")
    public ResponseEntity<User> deleteUser(@PathVariable long userId){
        User user = userService.findUserById(userId);

        if(user == null){
            return ResponseEntity.notFound().build();
        }
        userService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }



}
