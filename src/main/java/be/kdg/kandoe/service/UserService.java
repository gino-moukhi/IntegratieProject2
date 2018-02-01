package be.kdg.kandoe.service;

import be.kdg.kandoe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class UserService {
    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


}
