package be.kdg.kandoe.controller.rest;

import be.kdg.kandoe.domain.user.Authority;
import be.kdg.kandoe.domain.user.User;
import be.kdg.kandoe.dto.RequestUserDto;
import be.kdg.kandoe.dto.UserDto;
import be.kdg.kandoe.service.declaration.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<User> getUsers(){
        return userService.findUsers();
    }


    //GET ONE USER
    @GetMapping("/api/private/userid/{userId}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<RequestUserDto> getUser(HttpServletRequest request, @PathVariable long userId){
        String username = (String) request.getAttribute("username");
        boolean isAdmin = false;
        User tokenUser = userService.findUserByUsername(username);

        for(GrantedAuthority authority : tokenUser.getAuthorities()){
            if(authority.getAuthority().equalsIgnoreCase("ROLE_ADMIN")){
                isAdmin = true;
            }
        }

        if(!isAdmin && tokenUser.getUserId() != userId){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User user = userService.findUserById(userId);

        if(user == null){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        RequestUserDto requestUserDto = new RequestUserDto(user.getUsername(), user.getFirstName(), user.getLastName(), user.getEmail());
        return ResponseEntity.ok().body(requestUserDto);
    }


    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/api/private/users/username/{username}")
    public ResponseEntity<RequestUserDto> getUser(@PathVariable String username, HttpServletRequest request){
        //Token username
        String usernameFromToken = (String) request.getAttribute("username");
        User tokenUser = userService.findUserByUsername(usernameFromToken);
        boolean isAdmin = false;
        User requestUser = userService.findUserByUsername(username);


        for(GrantedAuthority authority : tokenUser.getAuthorities()){
            if(authority.getAuthority().equalsIgnoreCase("ROLE_ADMIN")){
                isAdmin = true;
            }
        }

        if(!isAdmin && !tokenUser.getUsername().equalsIgnoreCase(username)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if(requestUser == null){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        RequestUserDto requestUserDto = new RequestUserDto(requestUser.getUsername(), requestUser.getFirstName(), requestUser.getLastName(), requestUser.getEmail());
        return ResponseEntity.ok().body(requestUserDto);
    }

    //CREATE USER
//    @PostMapping("/api/private/users")
//    public User createUser(@Valid @RequestBody User user){
//        return userService.addUser(user);
//    }

    //UPDATE
    @PostMapping("/api/private/users/{userId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<User> updateUser(@PathVariable Long userId, @Valid @RequestBody UserDto changedUser){
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
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<User> deleteUser(@PathVariable Long userId){
        User user = userService.findUserById(userId);

        if(user == null){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        userService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }



}
