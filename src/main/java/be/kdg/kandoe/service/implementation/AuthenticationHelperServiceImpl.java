package be.kdg.kandoe.service.implementation;

import be.kdg.kandoe.common.DeviceProvider;
import be.kdg.kandoe.domain.user.User;
import be.kdg.kandoe.domain.user.UserTokenState;
import be.kdg.kandoe.dto.UserDto;
import be.kdg.kandoe.security.TokenHelper;
import be.kdg.kandoe.security.auth.JwtAuthenticationRequest;
import be.kdg.kandoe.service.declaration.AuthenticationHelperService;
import be.kdg.kandoe.service.declaration.UserService;
import be.kdg.kandoe.service.exception.CustomAuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class AuthenticationHelperServiceImpl implements AuthenticationHelperService {

    private UserService userService;

    private TokenHelper tokenHelper;

    private AuthenticationManager authenticationManager;

    private CustomUserDetailsService userDetailsService;

    private DeviceProvider deviceProvider;


    @Autowired
    public AuthenticationHelperServiceImpl(UserService userService, TokenHelper tokenHelper, AuthenticationManager authenticationManager, CustomUserDetailsService userDetailsService, DeviceProvider deviceProvider) {
        this.userService = userService;
        this.tokenHelper = tokenHelper;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.deviceProvider = deviceProvider;
    }

    @Override
    public UserTokenState authenticate(JwtAuthenticationRequest authenticationRequest, Device device){

        try{
            final Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.getUsername(),
                            authenticationRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // token creation
            User user = (User)authentication.getPrincipal();

            //Fill in roles
            StringBuilder roles = new StringBuilder();
            user.getAuthorities().forEach(role -> roles.append(role.getAuthority() + " "));

            String jws = tokenHelper.generateToken( user.getUsername(), device, roles.toString());
            int expiresIn = tokenHelper.getExpiredIn(device);

            return new UserTokenState(jws, expiresIn);

        }catch (AuthenticationException e){
            if (e.getMessage().toLowerCase().contains("bad credentials")) throw new CustomAuthenticationException("Username and password don't match");
            throw new CustomAuthenticationException("Something went wrong while trying to login", e);
        }
    }

    @Override
    public boolean checkUsernameCredentials(String username){
        return userService.usernameUsed(username);
    }

    @Override
    public boolean checkEmailCredentials(String email){
        return userService.emailUsed(email);
    }

    @Override
    public boolean register(UserDto userDto){
        boolean usernameGood = this.checkUsernameCredentials(userDto.getUsername());
        boolean emailGood = this.checkEmailCredentials(userDto.getEmail());

        if(!usernameGood && !emailGood){
            throw new CustomAuthenticationException("Username and email already used!");
        }
        else if(!usernameGood){
            throw new CustomAuthenticationException("Username is already used!");
        }
        else if(!emailGood){
            throw new CustomAuthenticationException("Email is already used!");
        }
        else{
            User user = new User(userDto);
            userDetailsService.addUser(user);
            return true;
        }
    }

    @Override
    public boolean isAdmin(User user) {
        for(GrantedAuthority authority : user.getAuthorities()){
            if(authority.getAuthority().equalsIgnoreCase("ROLE_ADMIN")){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean credentialsMatchToken(HttpServletRequest request) {
        String usernameToken = getUsernameFromTokem(request);
        User tokenUser = userService.findUserByUsername(usernameToken);
        return false;
    }

    //usernameClaimedUser is the username that was provided via url
    @Override
    public boolean userIsAllowedToAccessResource(HttpServletRequest request, String usernameClaimedUser) {
        String usernameToken = getUsernameFromTokem(request);
        User tokenUser = userService.findUserByUsername(usernameToken);

        //Check whether or not the user is an admin
        //(this is secure because we take the user found in the token)
        boolean isAdmin = isAdmin(tokenUser);

        //When an admin makes a request it is allowed
        //When it's not an admin the username from the path has to be the same as the one from the token
        if(!isAdmin && !tokenUser.getUsername().equalsIgnoreCase(usernameClaimedUser)){
            return false;
        }

        return true;
    }

    private String getUsernameFromTokem(HttpServletRequest request){
        return (String) request.getAttribute("username");
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setTokenHelper(TokenHelper tokenHelper) {
        this.tokenHelper = tokenHelper;
    }

    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public void setUserDetailsService(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    public void setDeviceProvider(DeviceProvider deviceProvider) {
        this.deviceProvider = deviceProvider;
    }
}
