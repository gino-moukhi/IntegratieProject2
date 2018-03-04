package be.kdg.kandoe.service;

import be.kdg.kandoe.domain.user.Authority;
import be.kdg.kandoe.domain.user.Gender;
import be.kdg.kandoe.domain.user.User;
import be.kdg.kandoe.repository.declaration.UserRepository;
import be.kdg.kandoe.service.declaration.UserService;
import be.kdg.kandoe.service.exception.UserServiceException;
import be.kdg.kandoe.service.implementation.UserServiceImpl;
import com.google.common.base.CharMatcher;
import org.hamcrest.Matchers;
import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

/*    private UserService userService;

    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    @Before
    public void setup(){
        userRepository = mock(UserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userRepository, passwordEncoder);
    }

    private boolean checkForUserDetails(User originalUser, User returnedUser){
        boolean containsUsername = originalUser.getUsername().equalsIgnoreCase(returnedUser.getUsername());
        boolean containsEmail = originalUser.getEmail().equalsIgnoreCase(returnedUser.getEmail());
        boolean containsFirstName = originalUser.getFirstName().equalsIgnoreCase(returnedUser.getFirstName());
        boolean containsLastName = originalUser.getLastName().equalsIgnoreCase(returnedUser.getLastName());
        boolean containsDay = originalUser.getDay() == returnedUser.getDay();
        boolean containsMonth = originalUser.getMonth() == returnedUser.getMonth();
        boolean containsYear = originalUser.getYear() == returnedUser.getYear();
        boolean containsGender = originalUser.getGender() == returnedUser.getGender();

        if(containsUsername &&
                containsEmail &&
                containsFirstName &&
                containsLastName &&
                containsDay &&
                containsMonth &&
                containsYear &&
                containsGender) return true;
        return false;

        //boolean containsPassword = json.contains(bob.getPassword());
        //boolean containsEncryptedPassword = json.contains(bob.getEncryptedPassword());


    }


    @Test
    public void getAllUsersTest(){
        List<User> users = new ArrayList<>();
        User bob = new User("Bob", "de bouwer", "bobdb", "bob.db@gmail.com",1, 2,1998,"", Gender.Male, null);
        User plop = new User("Plop", "Kabouter", "kabouterPlop", "plop@gmail.com",6, 9,1990,"", Gender.Male, null);
        User mindy = new User("Mindy", "Mega", "megaM", "mega.mindy@gmail.com",21, 4,2003,"", Gender.Female, null);

        users.add(bob);
        users.add(plop);
        users.add(mindy);

        when(userRepository.findAll()).thenReturn(users);
        List<User> returnedUsers = userService.findUsers();

        assertThat(returnedUsers.size(), is(3));
        returnedUsers.forEach(u -> assertThat(u, is(instanceOf(User.class))));
        assertThat(returnedUsers, IsIterableContainingInAnyOrder.containsInAnyOrder(users.toArray()));
        assertThat(true, is(checkForUserDetails(bob, users.get(0))));
        assertThat(true, is(checkForUserDetails(plop, users.get(1))));
        assertThat(true, is(checkForUserDetails(mindy, users.get(2))));
//        assertThat(returnedUsers.get(0), Matchers.samePropertyValuesAs(bob));
//        assertThat(returnedUsers.get(1), Matchers.samePropertyValuesAs(plop));
//        assertThat(returnedUsers.get(2), Matchers.samePropertyValuesAs(mindy));
    }

    @Test
    public void getAllUsersWhenThereIsOnlyOneTest(){
        List<User> users = new ArrayList<>();
        User bob = new User("Bob", "de bouwer", "bobdb", "bob.db@gmail.com",1, 2,1998,"", Gender.Male, null);
        users.add(bob);

        when(userRepository.findAll()).thenReturn(users);

        List<User> returnedUsers = userService.findUsers();

        assertThat(returnedUsers.size(), is(1));
        assertThat(returnedUsers.get(0), is(instanceOf(User.class)));
        assertThat(returnedUsers, IsIterableContainingInAnyOrder.containsInAnyOrder(users.toArray()));
        assertThat(true, is(checkForUserDetails(bob, returnedUsers.get(0))));
//        assertThat(returnedUsers.get(0), Matchers.samePropertyValuesAs(bob));
    }

    @Test
    public void getAllUsersWhenThereAreNoneTest(){
        List<User> returnedUsers = userService.findUsers();
        assertThat(returnedUsers, is(notNullValue()));
        assertThat(returnedUsers.size(), is(0));
    }


    @Test
    public void addUserTest(){
        User spongebob = new User("spongebob", "squarepants", "spongebob", "spongebob@hotmail.com7",14, 7,1986,"", Gender.Male, null);
        when(userRepository.save(spongebob)).thenReturn(spongebob);
        User savedUser = userService.addUser(spongebob);
        verify(userRepository, times(1)).save(spongebob);
        assertThat(savedUser, is(instanceOf(User.class)));
        assertThat(true, is(checkForUserDetails(savedUser, spongebob)));
//        assertThat(savedUser, Matchers.samePropertyValuesAs(spongebob));
    }

    //TODO Not sure if these should be here since these conditions are tested in the controller
    /*
    @Test
    public void addFaultyUserTest(){
        User spongebob = new User("spongebob", "squarepants", "spongebob", "spongebob@hotmail.com7",14, 7,1986,"", Gender.Male, null);

    }

    @Test
    public void addUserWithUsernameThatAlreadyExistsTest(){
        User spongebob = new User("spongebob", "squarepants", "spongebobSQ", "spongebob@hotmail.com7",14, 7,1986,"", Gender.Male, null);
        when(userRepository.save(spongebob)).thenReturn(spongebob);
        userService.addUser(spongebob);

        User anotherSpongebob = new User("spongebob", "squarepants", "spongebobSQ", "spongebob@hotmail.com7",14, 7,1986,"", Gender.Male, null);
        when(userRepository.save(anotherSpongebob)).thenReturn(anotherSpongebob);
        userService.addUser(anotherSpongebob);

        verify(userRepository, times(1)).save(spongebob);
        verify(userRepository, times(0)).save(anotherSpongebob);
    }

    @Test
    public void addUserWithEmailThatAlreadyExistsTest(){
        User spongebob = new User("spongebob", "squarepants", "spongebob", "spongebob@hotmail.com7",14, 7,1986,"", Gender.Male, null);

    }

    @Test
    public void addUserWithEmailAndUsernameThatAlreadyExistTest(){
        User spongebob = new User("spongebob", "squarepants", "spongebob", "spongebob@hotmail.com7",14, 7,1986,"", Gender.Male, null);

    }
    */
/*
    @Test
    public void getUserByIdTest(){
        User spongebob = new User("spongebob", "squarepants", "spongebob", "spongebob@hotmail.com7",14, 7,1986,"", Gender.Male, null);
        spongebob.setUserId(1);
        when(userRepository.findOne((long) 1)).thenReturn(spongebob);

        User returnedUser = userService.findUserById((long) 1);

        verify(userRepository, times(1)).findOne((long)1);
        assertThat(returnedUser, is(instanceOf(User.class)));
        assertThat(true, is(checkForUserDetails(returnedUser, spongebob)));
//        assertThat(returnedUser, Matchers.samePropertyValuesAs(spongebob));
    }

    @Test(expected = UserServiceException.class)
    public void getNonExistingUser(){
        User nonExistingUser = userService.findUserById((long) 2);
    }


    @Test
    public void getUserByUsernameTest(){
        User spongebob = new User("spongebob", "squarepants", "spongebob", "spongebob@hotmail.com",14, 7,1986,"", Gender.Male, null);
        when(userRepository.findUserByUsername(spongebob.getUsername())).thenReturn(spongebob);

        User returnedUser = userService.findUserByUsername(spongebob.getUsername());

        verify(userRepository, times(1)).findUserByUsername(spongebob.getUsername());
        assertThat(returnedUser, is(instanceOf(User.class)));
        assertThat(true, is(checkForUserDetails(returnedUser, spongebob)));
//        assertThat(returnedUser, Matchers.samePropertyValuesAs(spongebob));
    }

    @Test(expected = UserServiceException.class)
    public void getUserByUsernameThatDoesNotExist(){
        User user = userService.findUserByUsername("aUserNameThatDoesNotExist");
    }

    //TODO Uncomment once query in UserRepository is not case sensitive
//    @Test
//    public void getUserByUsernameWithCaps(){
//        User spongebob = new User("spongebob", "squarepants", "spongebob", "spongebob@hotmail.com",14, 7,1986,"", Gender.Male, null);
//        when(userRepository.findUserByUsername(spongebob.getUsername())).thenReturn(spongebob);
//        User user = userService.findUserByUsername("SponGeBob");
//        verify(userRepository, times(1)).findUserByUsername(spongebob.getUsername());
//        assertThat(user, is(spongebob));
//    }

    @Test
    public void getUserByEmailTest(){
        User spongebob = new User("spongebob", "squarepants", "spongebob", "spongebob@hotmail.com",14, 7,1986,"", Gender.Male, null);
        when(userRepository.findUserByEmail(spongebob.getEmail())).thenReturn(spongebob);

        User returnedUser = userService.findUserByEmail(spongebob.getEmail());

        verify(userRepository, times(1)).findUserByEmail(spongebob.getEmail());
        assertThat(returnedUser, is(instanceOf(User.class)));
        assertThat(true, is(checkForUserDetails(returnedUser, spongebob)));
//        assertThat(returnedUser, Matchers.samePropertyValuesAs(spongebob));
    }

    //TODO Uncomment once query in UserRepository is not case sensitive
//    @Test
//    public void getUserByUsernameWithCaps(){
//        User spongebob = new User("spongebob", "squarepants", "spongebob", "spongebob@hotmail.com",14, 7,1986,"", Gender.Male, null);
//        when(userRepository.findUserByEmail(spongebob.getEmail())).thenReturn(spongebob);
//        User user = userService.findUserByEmail("SpOngeBoB@hotmail.com");
//        verify(userRepository, times(1)).findUserByEmail(spongebob.getEmail());
//        assertThat(user, is(spongebob));
//    }
/*
    @Test
    public void getUserByRoleTest(){
        List<User> defaultUsers = new ArrayList<>();

        User spongebob = new User("spongebob", "squarepants", "spongebob", "spongebob@hotmail.com",14, 7,1986,"", Gender.Male, null);
        User bob = new User("Bob", "de bouwer", "bobdb", "bob.db@gmail.com",1, 2,1998,"", Gender.Male, null);

        defaultUsers.add(spongebob);
        defaultUsers.add(bob);

        when(userRepository.findUsersByRole(Authority.class)).thenReturn(defaultUsers);

        List<User> returnedUsers =  userService.findUsersByRole(Authority.class);
        verify(userRepository, times(1)).findUsersByRole(Authority.class);

        assertThat(returnedUsers.size(), is(2));
        returnedUsers.forEach(u -> assertThat(u, is(instanceOf(User.class))));
        assertThat(returnedUsers, IsIterableContainingInAnyOrder.containsInAnyOrder(defaultUsers.toArray()));
        assertThat(true, is(checkForUserDetails(returnedUsers.get(0), spongebob)));
        assertThat(true, is(checkForUserDetails(returnedUsers.get(1), bob)));
//        assertThat(returnedUsers.get(0), Matchers.samePropertyValuesAs(spongebob));
//        assertThat(returnedUsers.get(1), Matchers.samePropertyValuesAs(bob));

    }

    @Test
    public void removeUserTest(){
        User spongebob = new User("spongebob", "squarepants", "spongebob", "spongebob@hotmail.com",14, 7,1986,"", Gender.Male, null);
        when(userRepository.findOne((long) 1)).thenReturn(spongebob);

        userService.deleteUser((long) 1);
        verify(userRepository, times(1)).delete(spongebob);
    }

    @Test(expected = UserServiceException.class)
    public void removeNonExistingUser(){
        userService.deleteUser((long) 2);
    }

    @Test
    public void updateUserTest(){
        User spongebob = new User("sponge", "square", "sponge", "sp@hotmail.com7",15, 8,1987,"", Gender.Female, null);
        User updatedSpongebob = new User("spongebob", "squarepants", "spongebob", "spongebob@hotmail.com",14, 7,1986,"", Gender.Male, null);

        when(userRepository.findOne((long) 1)).thenReturn(spongebob);
        when(userRepository.save(updatedSpongebob)).thenReturn(updatedSpongebob);

        User returnedUser = userService.updateUser((long) 1, updatedSpongebob);

        verify(userRepository, times(1)).save(updatedSpongebob);
        assertThat(returnedUser, is(instanceOf(User.class)));
        assertThat(true, is(checkForUserDetails(returnedUser, updatedSpongebob)));
//        assertThat(returnedUser, Matchers.samePropertyValuesAs(updatedSpongebob));
    }

    @Test(expected = UserServiceException.class)
    public void updateUserTestWhereIdDoesNotMatchUser(){
        User spongebob = new User("sponge", "square", "sponge", "sp@hotmail.com7",15, 8,1987,"", Gender.Female, null);
        spongebob.setUserId(1);

        when(userRepository.findOne((long) 1)).thenReturn(spongebob);

        User updatedSpongebob = new User("spongebob", "squarepants", "spongebob", "spongebob@hotmail.com",14, 7,1986,"", Gender.Male, null);
        updatedSpongebob.setUserId(2);

        userService.updateUser((long) 1, updatedSpongebob);
    }

    @Test(expected = UserServiceException.class)
    public void updateNonExistingUser(){
        User spongebob = new User("sponge", "square", "sponge", "sp@hotmail.com7",15, 8,1987,"", Gender.Female, null);
        userService.updateUser((long) 2, spongebob);
    }

    @Test
    public void updatePasswordTest(){
        User spongebob = new User("sponge", "square", "sponge", "sp@hotmail.com7",15, 8,1987,"oldPassword", Gender.Female, null);
        spongebob.setUserId(1);

        User updatedSpongebob = new User("spongebob", "squarepants", "spongebob", "spongebob@hotmail.com",14, 7,1986,"newPassword", Gender.Male, null);
        updatedSpongebob.setUserId(1);

        when(userRepository.findOne((long)1)).thenReturn(spongebob);
        when(passwordEncoder.matches(spongebob.getPassword(), "oldPassword")).thenReturn(true);

        userService.updatePassword((long) 1, spongebob.getPassword(), updatedSpongebob.getPassword());
        verify(userRepository, times(1)).save(spongebob);
    }

    @Test(expected = UserServiceException.class)
    public void updatePasswordWhenPasswordOfUserIdDoesNotMatchTheOldPassword(){
        User spongebob = new User("sponge", "square", "sponge", "sp@hotmail.com7",15, 8,1987,"oldPassword", Gender.Female, null);
        spongebob.setUserId(1);

        User updatedSpongebob = new User("spongebob", "squarepants", "spongebob", "spongebob@hotmail.com",14, 7,1986,"newPassword", Gender.Male, null);
        updatedSpongebob.setUserId(1);

        userService.updatePassword((long) 2, spongebob.getPassword(), updatedSpongebob.getPassword());
    }
    */
}
