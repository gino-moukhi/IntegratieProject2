package be.kdg.kandoe.controller;

import be.kdg.kandoe.common.DeviceProvider;
import be.kdg.kandoe.common.TimeProvider;
import be.kdg.kandoe.domain.user.Authority;
import be.kdg.kandoe.domain.user.Gender;
import be.kdg.kandoe.domain.user.User;
import be.kdg.kandoe.security.TokenHelper;
import be.kdg.kandoe.service.declaration.UserService;
import be.kdg.kandoe.service.exception.UserServiceException;
import be.kdg.kandoe.service.implementation.CustomUserDetailsService;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class AuthenticationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomUserDetailsService userService;

    @Autowired
    private AuthenticationManager manager;

    @Test
    public void tryRegistrationTest() throws Exception{
        JSONObject user = new JSONObject("{\"firstName\":\"bob\",\"lastName\":\"de bouwer\",\"birthday\":\"1990-03-06\",\"gender\":\"Male\",\"email\":\"bob.db@gmail.com\",\"username\":\"bobdb\",\"password\":\"bobdbPassword\"}");

        when(userService.checkUsernameCredentials("bobdb")).thenReturn(true);
        when(userService.checkEmailCredentials("bob.db@gmail.com")).thenReturn(true);

        mockMvc.perform(post("/api/public/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content(user.toString()))
                .andExpect(status().isOk());
    }

    @Test
    public void tryRegistrationWithUsernameThatIsAlreadyTakenTest() throws Exception{
        JSONObject user = new JSONObject("{\"firstName\":\"bob\",\"lastName\":\"de bouwer\",\"birthday\":\"1990-03-06\",\"gender\":\"Male\",\"email\":\"bob.db@gmail.com\",\"username\":\"bobdb\",\"password\":\"bobdbPassword\"}");

        //This means the username has been taken
        when(userService.checkUsernameCredentials("bobdb")).thenReturn(false);

        //This means the email has not been taken
        when(userService.checkEmailCredentials("bob.db@gmail.com")).thenReturn(true);

        mockMvc.perform(post("/api/public/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(user.toString()))
                .andExpect(status().isConflict())
                .andExpect(content().string(containsString("Username is already used!")));
    }

    @Test
    public void tryRegistrationWithEmailThatIsAlreadyTakenTest() throws Exception{
        JSONObject user = new JSONObject("{\"firstName\":\"bob\",\"lastName\":\"de bouwer\",\"birthday\":\"1990-03-06\",\"gender\":\"Male\",\"email\":\"bob.db@gmail.com\",\"username\":\"bobdb\",\"password\":\"bobdbPassword\"}");

        //This means the username has not been taken
        when(userService.checkUsernameCredentials("bobdb")).thenReturn(true);

        //This means the email has been taken
        when(userService.checkEmailCredentials("bob.db@gmail.com")).thenReturn(false);

        mockMvc.perform(post("/api/public/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(user.toString()))
                .andExpect(status().isConflict())
                .andExpect(content().string(containsString("Email is already used!")));
    }

    @Test
    public void tryRegistrationWithEmailAndUsernameThatAreAlreadyTakenTest() throws Exception{
        JSONObject user = new JSONObject("{\"firstName\":\"bob\",\"lastName\":\"de bouwer\",\"birthday\":\"1990-03-06\",\"gender\":\"Male\",\"email\":\"bob.db@gmail.com\",\"username\":\"bobdb\",\"password\":\"bobdbPassword\"}");

        //This means the username has been taken
        when(userService.checkUsernameCredentials("bobdb")).thenReturn(false);

        //This means the email has been taken
        when(userService.checkEmailCredentials("bob.db@gmail.com")).thenReturn(false);

        mockMvc.perform(post("/api/public/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(user.toString()))
                .andExpect(status().isConflict())
                .andExpect(content().string(containsString("Username and email already used!")));
    }

    /*
    @Test
    @WithMockUser(username = "bobdb", password = "bobdbPassword")
    public void tryLoginTest() throws Exception{

        JSONObject test= new JSONObject("{\"firstName\":\"bob\",\"lastName\":\"de bouwer\",\"birthday\":\"1990-03-06\",\"gender\":\"Male\",\"email\":\"bob.db@gmail.com\",\"username\":\"bobdb\",\"password\":\"bobdbPassword\"}");
        when(userService.checkLogin("bobdb", "bobdbPassword")).thenReturn(true);

        mockMvc.perform(post("/api/public/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(test.toString()))
                .andExpect(status().isOk());
    }

    @Test
    public void tryLoginWithWrongCredentialsTest() throws Exception{
        JSONObject user = new JSONObject("{\"firstName\":\"bob\",\"lastName\":\"de bouwer\",\"birthday\":\"1990-03-06\",\"gender\":\"Male\",\"email\":\"bob.db@gmail.com\",\"username\":\"bobdb\",\"password\":\"bobdbPassword\"}");
        when(userService.checkLogin("bobbd", "bobdbPassword")).thenReturn(false);

        mockMvc.perform(post("/api/public/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(user.toString()))
                .andExpect(status().isConflict());
    }
*/

}
