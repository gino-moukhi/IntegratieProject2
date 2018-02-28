package be.kdg.kandoe.controller;


import be.kdg.kandoe.domain.user.Authority;
import be.kdg.kandoe.domain.user.Gender;
import be.kdg.kandoe.domain.user.User;
import be.kdg.kandoe.security.TokenHelper;
import be.kdg.kandoe.service.declaration.UserService;
import be.kdg.kandoe.service.implementation.CustomUserDetailsService;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@RunWith(SpringRunner.class)
@EnableWebSecurity
public class UserRestControllerTest {

    private MockMvc mockMvc;

    @Autowired
    protected WebApplicationContext wac;

    @MockBean
    private UserService userService;

    @Autowired
    private TokenHelper tokenHelper;

    private DeviceDummy deviceDummy;

    @MockBean
    private CustomUserDetailsService userDetailsService;

    private User bob;
    private User plop;
    private User mindy;
    private User sven;

    private List<User> users = new ArrayList<>();

    @Before
    public void setup(){
        deviceDummy = new DeviceDummy();
        deviceDummy.setNormal(true);

        mockMvc = MockMvcBuilders
                    .webAppContextSetup(wac)
                    .apply(springSecurity())
                    .build();

        //\"firstName\":\"bob\",\"lastName\":\"de bouwer\",\"birthday\":\"1990-03-06\",\"gender\":\"Male\",\"email\":\"bob.db@gmail.com\",\"username\":\"bobdb\",\"password\":\"bobdbPassword\"}");
        bob = new User("bob", "de bouwer", "bobdb", "bobdb@gmail.com", 6, 3, 1990, "bobdbPassword", Gender.Male, null);
        bob.setUserId(1);
        Authority bobRole = new Authority();
        bobRole.setName("ROLE_USER");
        bobRole.setUser(bob);
        bob.setAuthorities(Arrays.asList(bobRole));


        Authority plopRole = new Authority();
        plopRole.setName("ROLE_USER");
        plop = new User("plop", "kabouter", "kabouterPlop", "plop@gmail.com", 23, 7, 1992, "plopPassword", Gender.Male, null);
        plop.setUserId(2);
        plopRole.setUser(plop);
        plop.setAuthorities(Arrays.asList(plopRole));

        Authority mindyRole = new Authority();
        mindyRole.setName("ROLE_USER");
        mindy= new User("mindy", "mega", "megaMindy", "mega.mindy@gmail.com", 6, 9, 2004, "mindyPassword", Gender.Female, null);
        mindy.setUserId(3);
        mindyRole.setUser(mindy);
        mindy.setAuthorities(Arrays.asList(mindyRole));

        Authority adminRole = new Authority();
        adminRole.setName("ROLE_ADMIN");
        sven = new User("sven", "matthys", "sveneman", "matthys.sven@gmail.com", 6, 3, 1997, "svenPassword", Gender.Male, null);
        adminRole.setUser(sven);
        sven.setAuthorities(Arrays.asList(adminRole));


        users.add(bob);
        users.add(plop);
        users.add(mindy);
    }

    private boolean checkForUserDetails(String json, User user){
        //TODO remove passwords
        boolean containsUsername = json.contains(user.getUsername());
        boolean containsEmail = json.contains(user.getEmail());
        boolean containsFirstName = json.contains(user.getFirstName());
        boolean containsLastName = json.contains(user.getLastName());
        boolean containsUserId = json.contains(String.valueOf(user.getUserId()));
        boolean containsDay = json.contains(String.valueOf(user.getDay()));
        boolean containsMonth = json.contains(String.valueOf(user.getMonth()));
        boolean containsYear = json.contains(String.valueOf(user.getYear()));
        boolean containsGender = json.contains(String.valueOf(user.getGender()));

        if(containsUsername &&
                containsEmail &&
                containsFirstName &&
                containsLastName &&
                containsUserId &&
                containsDay &&
                containsMonth &&
                containsYear &&
                containsGender) return true;
        return false;

        //boolean containsPassword = json.contains(bob.getPassword());
        //boolean containsEncryptedPassword = json.contains(bob.getEncryptedPassword());


    }

    private boolean checkForUserDetailsWithLimitedAuthority(String json, User user){

        boolean containsUsername = json.contains(user.getUsername());
        boolean containsFirstName = json.contains(user.getFirstName());
        boolean containsLastName = json.contains(user.getLastName());
        boolean containsEmail = json.contains(user.getEmail());

        if (containsUsername &&
                containsFirstName &&
                containsLastName &&
                containsEmail) return true;
        return false;
    }

    private String createToken(String username, String roles){
        return tokenHelper.generateToken(username, deviceDummy, roles);
    }


    //IMPORTANT when we use @WithMockUser we don't have to provide a jwt token to access a resource (only the user role is checked)
    //Because of this we have test the need for jwt tokens in the three tests below
    //When we try to access a resource without provinding a jwt token we will receive a unAuthorized status (when we use @WithMockUser this won't be the case)
    @Test
    public void getAllUsersWithoutProvidingAToken() throws Exception{
        //This test is meant to test if we can access a resource when we don't provide a jwt token
        //This request should return a 401 unauthorized code

        when(userService.findUsers()).thenReturn(users);

        this.mockMvc.perform(
                get("/api/private/users")
                        .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized());
    }

    @Test
    public void getAllUsersWhileProvidingAToken() throws Exception{
        //This test is meant to test if we can access a resource when we provide a jwt token
        //This request should return a 200 ok code

        when(userDetailsService.loadUserByUsername(sven.getUsername())).thenReturn(sven);
        when(userService.findUserByUsername(sven.getUsername())).thenReturn(sven);

        String jwtToken = createToken(sven.getUsername(), "ROLE_ADMIN");

        this.mockMvc.perform(
                get("/api/private/users")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    public void getAllUsersWithAnonymousAccountAndJwtTest() throws Exception{
        //This request should return a 401 unauthorized code

        when(userDetailsService.loadUserByUsername(sven.getUsername())).thenReturn(sven);
        when(userService.findUserByUsername(sven.getUsername())).thenReturn(sven);

        String jwtToken = createToken(sven.getUsername(), "ROLE_ADMIN");

        mockMvc.perform(
                get("/api/private/users")
                        .with(csrf())
                        .header("Authorization", "Bearer " + jwtToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }




    //getUsers tests
    @Test
    @WithAnonymousUser
    public void getAllUsersWithAnonymousAccountTest() throws Exception{
        //This request should return a 401 unauthorized code

        mockMvc.perform(
                get("/api/private/users")
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void getAllUsersWithAdminAccountTest() throws Exception{
        //This request should return a list of all users in the system (resource is only available for admins)


        when(userDetailsService.loadUserByUsername("sven")).thenReturn(sven);
        when(userService.findUserByUsername("sven")).thenReturn(sven);

        String jwtToken = createToken("sven", "ROLE_ADMIN");

        when(userService.findUsers()).thenReturn(users);

        MvcResult result = mockMvc.perform(
                            get("/api/private/users")
                            .header("Authorization", "Bearer " + jwtToken)
                            .with(csrf())
                            .accept(MediaType.APPLICATION_JSON))
                            .andExpect(status().isOk())
                            .andReturn();
        String jsonResponse = result.getResponse().getContentAsString();
        assertThat(true, is(checkForUserDetails(jsonResponse, bob)));
        assertThat(true, is(checkForUserDetails(jsonResponse, plop)));
        assertThat(true, is(checkForUserDetails(jsonResponse, mindy)));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void getAllUsersWithUserAccountTest() throws Exception{
        //This request should return 403 forbidden since this resource is only available for admins

        MvcResult result = mockMvc.perform(
                get("/api/private/users")
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn();
        String jsonResponse = result.getResponse().getContentAsString();
        assertThat(jsonResponse, is(""));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void getAllUsersWithAdminAccountWhenThereAreNoneTest() throws Exception{
        //This request should return an empty list --> An empty list in json is represented by '[]' (resource is only available for admins)

        when(userService.findUsers()).thenReturn(new ArrayList<>());

        MvcResult result = mockMvc.perform(
                get("/api/private/users")
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String jsonResponse = result.getResponse().getContentAsString();
        assertThat(jsonResponse, is("[]"));
    }




    //getUser based on userId tests
    @Test
    @WithAnonymousUser
    public void getUserByUserIdWithAnonymousAccount() throws Exception{
        this.mockMvc.perform(
                get("/api/private/userid/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void getUserByUserIdWithAdminAccount() throws Exception{
        deviceDummy.setNormal(true);

        when(userDetailsService.loadUserByUsername(sven.getUsername())).thenReturn(sven);
        when(userService.findUserByUsername(sven.getUsername())).thenReturn(sven);
        when(userService.findUserById(plop.getUserId())).thenReturn(plop);

        String jwtToken = createToken(sven.getUsername(), "ROLE_ADMIN");

        MvcResult result = mockMvc.perform(
                get("/api/private/userid/2")
                        .header("Authorization", "Bearer " + jwtToken)
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String jsonResponse = result.getResponse().getContentAsString();
        assertThat(true, is(checkForUserDetailsWithLimitedAuthority(jsonResponse, plop)));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void getOtherUserByUserIdWithUserAccount() throws Exception{
        //A user should be able to access it's own settings.
        //This means that if the user makes a HTTP Get request to the resource it will only be allowed if the provided userId is the userId of that user
        //In this test this isn't the case an thus the status should be unauthorized

        deviceDummy.setNormal(true);

        when(userDetailsService.loadUserByUsername(mindy.getUsername())).thenReturn(mindy);
        when(userService.findUserByUsername(mindy.getUsername())).thenReturn(mindy);
        when(userService.findUserById(plop.getUserId())).thenReturn(plop);

        String jwtToken = createToken(mindy.getUsername(), "ROLE_USER");

        MvcResult result = mockMvc.perform(
                get("/api/private/userid/2")
                        .header("Authorization", "Bearer " + jwtToken)
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();
        String jsonResponse = result.getResponse().getContentAsString();
        assertThat(jsonResponse, is(""));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void getUserByUserIdWithUserAccount() throws Exception{
        //A user should be able to access it's own settings.
        // This means that if the user makes a HTTP Get request to the resource it will only be allowed if the provided userId is the userId of that user
        //Here it is the case and thus it should return the user details

        deviceDummy.setNormal(true);

        when(userDetailsService.loadUserByUsername(mindy.getUsername())).thenReturn(mindy);
        when(userService.findUserByUsername(mindy.getUsername())).thenReturn(mindy);
        when(userService.findUserById(mindy.getUserId())).thenReturn(mindy);

        String jwtToken = createToken(mindy.getUsername(), "ROLE_USER");

        MvcResult result = mockMvc.perform(
                get("/api/private/userid/" + mindy.getUserId())
                        .header("Authorization", "Bearer " + jwtToken)
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String jsonResponse = result.getResponse().getContentAsString();
        assertThat(true, is(checkForUserDetailsWithLimitedAuthority(jsonResponse, mindy)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void getNonExistingUserByUserIdWithAdminAccount() throws Exception{
        //If the provided userId doesn't link to an existing user then we should get a notFound status

        deviceDummy.setNormal(true);

        when(userDetailsService.loadUserByUsername(sven.getUsername())).thenReturn(sven);
        when(userService.findUserByUsername(sven.getUsername())).thenReturn(sven);
        when(userService.findUserById(4l)).thenReturn(null);

        String jwtToken = createToken(sven.getUsername(), "ROLE_ADMIN");

        MvcResult result = mockMvc.perform(
                get("/api/private/userid/4")
                        .header("Authorization", "Bearer " + jwtToken)
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();
        String jsonResponse = result.getResponse().getContentAsString();
        assertThat(jsonResponse, is(""));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void getNonExistingUserByUserIdWithUserAccount() throws Exception{
        //If the provided userId doesn't link to an existing user then we should get a notFound status
        //In this case we should get a unauthorized status because a user is trying to get the information of another user

        deviceDummy.setNormal(true);

        when(userDetailsService.loadUserByUsername(mindy.getUsername())).thenReturn(mindy);
        when(userService.findUserByUsername(mindy.getUsername())).thenReturn(mindy);
        when(userService.findUserById(4l)).thenReturn(null);

        String jwtToken = createToken(mindy.getUsername(), "ROLE_USER");

        MvcResult result = mockMvc.perform(
                get("/api/private/userid/4")
                        .header("Authorization", "Bearer " + jwtToken)
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();
        String jsonResponse = result.getResponse().getContentAsString();
        assertThat(jsonResponse, is(""));
    }

    @Test
    @WithAnonymousUser
    public void getNonExistingUserByUserIdWithAnonymousAccount() throws Exception{
        this.mockMvc.perform(
                get("/api/private/userid/4")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }




    //getUser based on username tests
    @Test
    @WithAnonymousUser
    public void getUserByUsernameWithAnonymousAccount() throws Exception{
        this.mockMvc.perform(
                get("/api/private/users/username/" + plop.getUsername())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void getUserByUsernameWithAdminAccount() throws Exception{
        deviceDummy.setNormal(true);

        when(userDetailsService.loadUserByUsername(sven.getUsername())).thenReturn(sven);
        when(userService.findUserByUsername(sven.getUsername())).thenReturn(sven);
        when(userService.findUserByUsername(plop.getUsername())).thenReturn(plop);

        String jwtToken = createToken(sven.getUsername(), "ROLE_ADMIN");

        MvcResult result = mockMvc.perform(
                get("/api/private/users/username/" + plop.getUsername())
                        .header("Authorization", "Bearer " + jwtToken)
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String jsonResponse = result.getResponse().getContentAsString();
        assertThat(true, is(checkForUserDetailsWithLimitedAuthority(jsonResponse, plop)));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void getOtherUserByUsernameWithUserAccount() throws Exception{
        //A user should be able to access it's own settings.
        //This means that if the user makes a HTTP Get request to the resource it will only be allowed if the provided userId is the userId of that user
        //In this test this isn't the case an thus the status should be unauthorized

        deviceDummy.setNormal(true);

        when(userDetailsService.loadUserByUsername(mindy.getUsername())).thenReturn(mindy);
        when(userService.findUserByUsername(mindy.getUsername())).thenReturn(mindy);
        when(userService.findUserByUsername(plop.getUsername())).thenReturn(plop);

        String jwtToken = createToken(mindy.getUsername(), "ROLE_USER");

        MvcResult result = mockMvc.perform(
                get("/api/private/users/username/" + plop.getUsername())
                        .header("Authorization", "Bearer " + jwtToken)
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();
        String jsonResponse = result.getResponse().getContentAsString();
        assertThat(jsonResponse, is(""));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void getUserByUsernameWithUserAccount() throws Exception{
        //A user should be able to access it's own settings.
        // This means that if the user makes a HTTP Get request to the resource it will only be allowed if the provided userId is the userId of that user
        //Here it is the case and thus it should return the user details

        deviceDummy.setNormal(true);

        when(userDetailsService.loadUserByUsername(mindy.getUsername())).thenReturn(mindy);
        when(userService.findUserByUsername(mindy.getUsername())).thenReturn(mindy);
        when(userService.findUserByUsername(mindy.getUsername())).thenReturn(mindy);

        String jwtToken = createToken(mindy.getUsername(), "ROLE_USER");

        MvcResult result = mockMvc.perform(
                get("/api/private/users/username/" + mindy.getUsername())
                        .header("Authorization", "Bearer " + jwtToken)
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String jsonResponse = result.getResponse().getContentAsString();
        assertThat(true, is(checkForUserDetailsWithLimitedAuthority(jsonResponse, mindy)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void getNonExistingUserByUsernameWithAdminAccount() throws Exception{
        //If the provided username doesn't link to an existing user then we should get a notFound status

        deviceDummy.setNormal(true);

        when(userDetailsService.loadUserByUsername(sven.getUsername())).thenReturn(sven);
        when(userService.findUserByUsername(sven.getUsername())).thenReturn(sven);
        when(userService.findUserByUsername("imNotInTheSystem")).thenReturn(null);

        String jwtToken = createToken(sven.getUsername(), "ROLE_ADMIN");

        MvcResult result = mockMvc.perform(
                get("/api/private/users/username/" + "imNotInTheSystem")
                        .header("Authorization", "Bearer " + jwtToken)
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();
        String jsonResponse = result.getResponse().getContentAsString();
        assertThat(jsonResponse, is(""));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void getNonExistingUserByUsernameWithUserAccount() throws Exception{
        //If the provided username doesn't link to an existing user then we should get a notFound status
        //In this case we should get a unauthorized status because a user is trying to get the information of another user

        deviceDummy.setNormal(true);

        when(userDetailsService.loadUserByUsername(mindy.getUsername())).thenReturn(mindy);
        when(userService.findUserByUsername(mindy.getUsername())).thenReturn(mindy);
        when(userService.findUserByUsername("imNotInTheSystem")).thenReturn(null);

        String jwtToken = createToken(mindy.getUsername(), "ROLE_USER");

        MvcResult result = mockMvc.perform(
                get("/api/private/users/username/" + "imNotInTheSystem")
                        .header("Authorization", "Bearer " + jwtToken)
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();
        String jsonResponse = result.getResponse().getContentAsString();
        assertThat(jsonResponse, is(""));
    }

    @Test
    @WithAnonymousUser
    public void getNonExistingUserByUsernameWithAnonymousAccount() throws Exception{
        this.mockMvc.perform(
                get("/api/private/users/username/" + "imNotInTheSystem")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }




    //updateUser tests
//    @Test
//    public void updateUser() throws Exception{
//        User updatedBobUser = new User ("bobina", "het bouwertje", "bobinahb", "bobina@gmail.com", 1, 2, 1990, "bobyWachtwoord", Gender.Female, null);
//
//        JSONObject updatedBob = new JSONObject();
//        updatedBob.put("firstName", "bobina");
//        updatedBob.put("lastName", "het bouwertje");
//        updatedBob.put("username", "bobinahb");
//        updatedBob.put("email", "bobina@gmail.com");
//        updatedBob.put("year", 1990);
//        updatedBob.put("month", 2);
//        updatedBob.put("day", 1);
//        updatedBob.put("gender", "Female");
//        updatedBob.put("password", "bobyWachtwoord");
//
//
//        when(userDetailsService.loadUserByUsername(bob.getUsername())).thenReturn(bob);
//        when(userService.findUserByUsername(bob.getUsername())).thenReturn(bob);
//        when(userService.findUserById(bob.getUserId())).thenReturn(bob);
//
//        String jwtToken = createToken(String.valueOf(bob.getUsername()), "ROLE_USER");
//
//        mockMvc.perform(
//                post("/api/private/users/" + bob.getUserId())
//                .content(updatedBob.toString())
//                .header("Authorization", "Bearer " + jwtToken)
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk());
//    }





    //deleteUser tests
//    @Test
//    public void deleteUserWithAdminAccount() throws Exception{
//        when(userDetailsService.loadUserByUsername(sven.getUsername())).thenReturn(sven);
//        when(userService.findUserByUsername(sven.getUsername())).thenReturn(sven);
//        when(userService.findUserById(2l)).thenReturn(plop);
//
//        String jwtToken = createToken(sven.getUsername(), "ROLE_ADMIN");
//
//        MvcResult result = mockMvc.perform(
//                delete("api/private/users/2")
//                .with(csrf())
//                        .header("Authorization", "Bearer " + jwtToken))
//                .andExpect(status().isOk())
//                .andReturn();
//    }






    //createUser tests
//    @Test
//    public void createValidUser() throws Exception{
//        //JSONObject user = new JSONObject("{\"firstName\":\"bob\",\"lastName\":\"de bouwer\",\"birthday\":\"1990-03-06\",\"gender\":\"Male\",\"email\":\"bob.db@gmail.com\",\"username\":\"bobdb\",\"password\":\"bobdbPassword\"}");
//        JSONObject user = new JSONObject();
//        user.put("firstName", "sven");
//        user.put("lastName", "matthys");
//        user.put("username", "sveneman");
//        user.put("email", "matthys.sven@gmail.com");
//        user.put("year", 1997);
//        user.put("month", 3);
//        user.put("day", 6);
//        user.put("gender", "Male");
//        user.put("password", "mijnSuperSecretPassword");
//
//        mockMvc.perform(
//                post("/api/private/users")
//                .content(user.toString()))
//                .andExpect(status().isOk());
//
//    }



}
