package be.kdg.kandoe.controller.rest;

import be.kdg.kandoe.domain.user.Authority;
import be.kdg.kandoe.domain.user.User;
import be.kdg.kandoe.dto.RequestUserDto;
import be.kdg.kandoe.dto.UpdateuserDto;
import be.kdg.kandoe.dto.UserDto;
import be.kdg.kandoe.service.declaration.AuthenticationHelperService;
import be.kdg.kandoe.service.declaration.StorageService;
import be.kdg.kandoe.service.declaration.UserService;
import com.google.common.net.MediaType;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import sun.misc.IOUtils;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.List;

@RestController
public class UserRestController {
    private final Logger logger = Logger.getLogger(UserRestController.class);
    private final UserService userService;
    private final StorageService storageService;
    private final AuthenticationHelperService authenticationHelperService;


    @Autowired
    public UserRestController(UserService userService, StorageService storageService, AuthenticationHelperService authenticationHelperService) {
        this.userService = userService;
        this.storageService = storageService;
        this.authenticationHelperService = authenticationHelperService;
    }


    //GET ALL USERS
    @GetMapping("/api/private/users")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<User> getUsers(){
        return userService.findUsers();
    }


    //GET ONE USER ON USERID
    @GetMapping("/api/private/userid/{userId}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<RequestUserDto> getUser(HttpServletRequest request, @PathVariable long userId){

        User requestUser = userService.findUserById(userId);
        String username = "";

        if (requestUser != null) username = requestUser.getUsername();

        if (!authenticationHelperService.userIsAllowedToAccessResource(request, username)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        };

        if(requestUser == null){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        RequestUserDto requestUserDto = new RequestUserDto(requestUser.getUsername(), requestUser.getFirstName(), requestUser.getLastName(), requestUser.getEmail(), requestUser.getBirthday(), requestUser.getGender());

        return ResponseEntity.ok().body(requestUserDto);
    }


    //GET ONE USER ON USERNAME
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/api/private/users/username/{username}")
    public ResponseEntity<RequestUserDto> getUser(@PathVariable String username, HttpServletRequest request){
        User requestUser = userService.findUserByUsername(username);

        if (!authenticationHelperService.userIsAllowedToAccessResource(request, username)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        };

        if(requestUser == null){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        RequestUserDto requestUserDto = new RequestUserDto(requestUser.getUsername(), requestUser.getFirstName(), requestUser.getLastName(), requestUser.getEmail(), requestUser.getBirthday(), requestUser.getGender());

        return ResponseEntity.ok().body(requestUserDto);
    }

    //CREATE USER
//    @PostMapping("/api/private/users")
//    public User createUser(@Valid @RequestBody User user){
//        return userService.addUser(user);
//    }

    //UPDATE
//    @PutMapping("/api/private/users/{username}")
//    @PreAuthorize("hasRole('ROLE_USER')")
//    public ResponseEntity<UpdateuserDto> updateUser(@PathVariable String username, @Valid @RequestBody UpdateuserDto changedUser, HttpServletRequest request){
//        User userBasedOnUrlUserId = userService.findUserById(user);
//
//        String usernameFromToken = (String) request.getAttribute("username");
//        User tokenUser = userService.findUserByUsername(usernameFromToken);
//
//        if(userBasedOnUrlUserId == null){
//            return ResponseEntity.notFound().build();
//        }
//
//        if(!userBasedOnUrlUserId.getUsername().equalsIgnoreCase(changedUser.getUsername()) ||
//                !userBasedOnUrlUserId.getUsername().equalsIgnoreCase(usernameFromToken) ||
//                !changedUser.getUsername().equalsIgnoreCase(usernameFromToken)){
//            return ResponseEntity.status(HttpStatus.CONFLICT).build();
//        }
//
//        //Omzetten DTO naar user object
//        User updatedUser = new User(changedUser);
//        updatedUser.setUserId(tokenUser.getUserId());
//        updatedUser.setUsername(tokenUser.getUsername());
//        updatedUser.setEmail(tokenUser.getEmail());
//        updatedUser.setAuthorities(tokenUser.getUserRoles());
//
//        User savedUser = userService.updateUser(userId, updatedUser);
//        UpdateuserDto updateuserDto = new UpdateuserDto(savedUser);
//
////        User savedUser = userService.saveUser(updatedUser);
////        UpdateuserDto updateuserDto = new UpdateuserDto(savedUser);
//        return ResponseEntity.ok(updateuserDto);
//    }



    //UPDATE USER
    @PutMapping("/api/private/users/{username}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<RequestUserDto> updateUser(@PathVariable String username, @Valid @RequestBody UserDto changedUser, HttpServletRequest request){
//        User userBasedOnUsername = userService.findUserByUsername(username);
//
//        String usernameFromToken = (String) request.getAttribute("username");
//        User tokenUser = userService.findUserByUsername(usernameFromToken);
//
//        if(userBasedOnUsername == null){
//            return ResponseEntity.notFound().build();
//        }


        String usernameFromToken = (String) request.getAttribute("username");
        User requestUser = userService.findUserByUsername(username);

        if (!authenticationHelperService.userIsAllowedToAccessResource(request, username)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        };

        if(requestUser == null){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        if(!requestUser.getUsername().equalsIgnoreCase(changedUser.getUsername()) ||
                !requestUser.getUsername().equalsIgnoreCase(usernameFromToken) ||
                !changedUser.getUsername().equalsIgnoreCase(usernameFromToken)){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }




        //Omzetten DTO naar user object
        User updatedUser = new User(changedUser);
        updatedUser.setUserId(requestUser.getUserId());
        updatedUser.setUsername(requestUser.getUsername());
        updatedUser.setEmail(requestUser.getEmail());
        updatedUser.setAuthorities(requestUser.getUserRoles());

        User savedUser = userService.updateUser(requestUser.getUserId(), updatedUser);
        RequestUserDto requestUserDto = new RequestUserDto(savedUser.getUsername(), savedUser.getFirstName(), savedUser.getLastName(), savedUser.getEmail());
        return ResponseEntity.ok(requestUserDto);
    }





    //DELETE
//    @DeleteMapping("api/private/users/{userId}")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    public ResponseEntity<User> deleteUser(@PathVariable Long userId){
//        User user = userService.findUserById(userId);
//
//        if(user == null){
//            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
//        }
//
//        userService.deleteUser(userId);
//        return ResponseEntity.ok().build();
//    }



}
