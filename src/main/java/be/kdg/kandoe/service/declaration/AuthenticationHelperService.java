package be.kdg.kandoe.service.declaration;

import be.kdg.kandoe.domain.user.User;

import javax.servlet.http.HttpServletRequest;

public interface AuthenticationHelperService {
    boolean isAdmin(User user);

    boolean credentialsMatchToken(HttpServletRequest request);

    boolean userIsAllowedToAccessResource(HttpServletRequest request, String usernameClaimedUser);

}
