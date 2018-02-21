package be.kdg.kandoe.service.implementation;

import be.kdg.kandoe.domain.user.User;
import be.kdg.kandoe.domain.user.role.Client;
import be.kdg.kandoe.repository.declaration.UserRepository;
import be.kdg.kandoe.service.exception.UserServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements be.kdg.kandoe.service.declaration.UserService {

    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User findUserById(Long id) throws UserServiceException {
        User user = userRepository.findOne(id);
        if (user == null)
            throw new UserServiceException("User not found");
        return user;
    }

    @Override
    public List<User> findUsers() {
        List<User> users = userRepository.findAll();
        if(users == null){
            return new ArrayList<>();
        }
        else{
            return users;
        }
    }

    @Override
    public User findUserByUsername(String username) throws UserServiceException {
        User user = userRepository.findUserByUsername(username);

        if (user == null)
            throw new UserServiceException("User not found");

        return user;
    }

    @Override
    public List<User> findUsersByRole(Class c) {
        return userRepository.findUsersByRole(c);
    }

    @Override
    public User saveUser(User user) throws UserServiceException {
        User u = userRepository.save(user);
        if (u == null)
            throw new UserServiceException("User not saved");
        return u;
    }

    //TODO fix
    @Override
    public User updateUser(Long userId, User user) throws UserServiceException {
        User u = userRepository.findOne(userId);

        if(u == null){
            throw new UserServiceException("User not found");
        }

        if(user.getUserId() != u.getUserId()){
            throw new UserServiceException("User is not the same user");
        }

        return userRepository.save(user);
    }

    @Override
    public User addUser(User user) throws UserServiceException {
        Client client = new Client();
        user.setEncryptedPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Arrays.asList(client));
        client.setUser(user);
        return this.saveUser(user);
    }

    @Override
    public void deleteUser(Long userId) throws UserServiceException {
        User user = userRepository.findOne(userId);
        if (user == null)
            throw new UserServiceException("User not found");

        userRepository.delete(user);
    }

    @Override
    public void checkLogin(Long userId, String currentPassword) throws UserServiceException {
        User u = userRepository.findOne(userId);
        if (u == null || !passwordEncoder.matches(currentPassword, u.getEncryptedPassword())) {
            throw new UserServiceException(("Username or password are wrong for user " + userId));
        }
    }


//    @Override
//    public void checkLogin(String username, String password) throws UserServiceException {
//        User u = userRepository.findUserByUsername(username);
//        if(u == null || !passwordEncoder.matches(password, u.getEncryptedPassword())){
//            throw new UserServiceException("Username and password are incorrect for user " + username);
//        }
//    }

    @Override
    public boolean checkLogin(String username, String password) throws UserServiceException {
        User u = userRepository.findUserByUsername(username);
        if(u == null || !passwordEncoder.matches(password, u.getEncryptedPassword())){
            return false;
        }
        return true;
    }

    @Override
    public void updatePassword(Long userId, String oldPassword, String newPassword) throws UserServiceException {
        User u = userRepository.findOne(userId);
        checkLogin(userId, oldPassword);
        u.setEncryptedPassword(passwordEncoder.encode(newPassword));
        userRepository.save(u);
    }


    @Override
    public User findUserByEmail(String email) throws UserServiceException {
        User user = userRepository.findUserByEmail(email);
        if (user == null)
            throw new UserServiceException("User not found");
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User u = userRepository.findUserByUsername(username);
        if (u == null) throw new UsernameNotFoundException("No such user: " + username);
        return u;
    }

    @Override
    public boolean checkUsernameCredentials(String username){
        User sameUsernameUser = userRepository.findUserByUsername(username);
        return sameUsernameUser == null;
    }

    @Override
    public boolean checkEmailCredentials(String email){
        User sameEmailUser = userRepository.findUserByEmail(email);
        return sameEmailUser == null;
    }
}
