package be.kdg.kandoe.service.declaration;

import be.kdg.kandoe.domain.user.User;
import be.kdg.kandoe.domain.user.UserTokenState;
import be.kdg.kandoe.dto.UserDto;
import be.kdg.kandoe.security.auth.JwtAuthenticationRequest;
import be.kdg.kandoe.service.exception.CustomAuthenticationException;
import org.springframework.mobile.device.Device;

import javax.servlet.http.HttpServletRequest;

public interface AuthenticationHelperService {
    UserTokenState authenticate(JwtAuthenticationRequest authenticationRequest, Device device) throws CustomAuthenticationException;

    boolean isAdmin(User user);

    boolean credentialsMatchToken(HttpServletRequest request);

    boolean userIsAllowedToAccessResource(HttpServletRequest request, String usernameClaimedUser);

    boolean checkUsernameCredentials(String username);

    boolean checkEmailCredentials(String email);

    boolean register(UserDto userDto) throws CustomAuthenticationException;
}
