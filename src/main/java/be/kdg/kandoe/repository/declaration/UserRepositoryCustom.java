package be.kdg.kandoe.repository.declaration;


import be.kdg.kandoe.domain.user.User;

import java.util.List;


public interface UserRepositoryCustom {
    List<User> findUsersByRole(Class c);
}
