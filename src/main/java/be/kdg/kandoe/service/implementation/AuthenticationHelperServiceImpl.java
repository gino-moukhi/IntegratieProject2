package be.kdg.kandoe.service.implementation;

import be.kdg.kandoe.domain.user.User;
import be.kdg.kandoe.service.declaration.AuthenticationHelperService;
import be.kdg.kandoe.service.declaration.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class AuthenticationHelperServiceImpl implements AuthenticationHelperService {

    private final UserService userService;

    @Autowired
    public AuthenticationHelperServiceImpl(UserService userService) {
        this.userService = userService;
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
}
