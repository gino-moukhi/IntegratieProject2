package be.kdg.kandoe.repository;

import be.kdg.kandoe.domain.user.Gender;
import be.kdg.kandoe.domain.user.User;
import be.kdg.kandoe.domain.user.role.Administrator;
import be.kdg.kandoe.domain.user.role.Client;
import be.kdg.kandoe.domain.user.role.Role;
import be.kdg.kandoe.repository.declaration.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRepositoryTest {
//    @Autowired
//    private UserRepository userRepository;
//
//    private User bob;
//    private User plop;
//    private User mindy;
//
//    @Before
//    public void setup(){
//        List<Role> roles = new ArrayList<>();
//        roles.add(new Client());
//        bob = new User("Bob", "de bouwer", "bobdb", "bob.db@gmail.com",1, 2,1998,"", Gender.Male, Arrays.asList(new Administrator()));
//        bob.setUserId(1);
//
//        plop = new User("Plop", "Kabouter", "kabouterPlop", "plop@gmail.com",6, 9,1990,"", Gender.Male, Arrays.asList(new Client()));
//        plop.setUserId(2);
//
//        mindy = new User("Mindy", "Mega", "megaM", "mega.mindy@gmail.com",21, 4,2003,"", Gender.Female, Arrays.asList(new Client()));
//        mindy.setUserId(3);
//
//        userRepository.save(Arrays.asList(bob, plop, mindy));
//    }
//
//    @After
//    public void tearDown() throws Exception {
//        bob = null;
//        plop = null;
//        mindy = null;
//        userRepository.deleteAll();
//    }
//
//    @Test
//    public void getUserWithUsernameTest(){
//        User user = userRepository.findUserByUsername(bob.getUsername());
//        assertThat(user.getUsername(), is(bob.getUsername()));
//    }
//
//    @Test
//    public void getUserWithEmailTest(){
//        User user = userRepository.findUserByEmail(plop.getEmail());
//        assertThat(user.getEmail(), is(equalTo(plop.getEmail())));
//    }
//
//    @Test
//    public void getUserWithIdTest(){
//        User user = userRepository.findOne((long) 1);
//        assertThat(user.getUserId(), is(equalTo((long)1)));
//    }
//
//    //TODO doesn't work as long as the user_user_Id field in role isn't filled in
//    @Test
//    public void getUsersByRolesTest(){
//        List<User> clients = userRepository.findUsersByRole(Client.class);
//        List<User> administrators = userRepository.findUsersByRole(Administrator.class);
//        assertThat(clients.size(), is(2));
//        assertThat(administrators.size(), is(1));
//    }

    //Todo fix for some reason fails when you run all tests together
//    @Test
//    public void deleteUserTest(){
//        int amountOfUsersBefore = userRepository.findAll().size();
//        userRepository.delete((long) 2);
//        int amountOfUsersAfter = userRepository.findAll().size();
//        List<User> users = userRepository.findAll();
//        assertThat(amountOfUsersAfter, is(amountOfUsersBefore-1));
//    }


}
