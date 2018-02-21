package be.kdg.kandoe.repository.declaration;


import be.kdg.kandoe.domain.user.User;

import java.util.List;

/**
 * Extra interface to support customization of Spring's Data interfaces
 * https://programmeren3-repaircafe.rhcloud.com/repair-cafe-applicatie/repair-cafe-backend/backend-persistence-layer/
 */
public interface UserRepositoryCustom {
    List<User> findUsersByRole(Class c);
}
