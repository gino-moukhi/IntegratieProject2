package be.kdg.kandoe.controller;

import be.kdg.kandoe.common.DeviceProvider;
import be.kdg.kandoe.common.TimeProvider;
import be.kdg.kandoe.domain.user.Authority;
import be.kdg.kandoe.domain.user.Gender;
import be.kdg.kandoe.domain.user.User;
import be.kdg.kandoe.dto.UserDto;
import be.kdg.kandoe.security.TokenHelper;
import be.kdg.kandoe.service.declaration.AuthenticationHelperService;
import be.kdg.kandoe.service.declaration.UserService;
import be.kdg.kandoe.service.exception.CustomAuthenticationException;
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
import java.util.Calendar;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.Matchers.any;
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

//    @MockBean
//    private CustomUserDetailsService userService;

    @MockBean
    private UserService userService;

    @MockBean
    private AuthenticationHelperService authenticationHelperService;

    @Autowired
    private AuthenticationManager manager;

    @Test
    public void tryRegistrationTest() throws Exception{
        JSONObject user = new JSONObject("{\"firstName\":\"bob\",\"lastName\":\"de bouwer\",\"birthday\":\"1990-03-06\",\"gender\":\"Male\",\"email\":\"bob.db@gmail.com\",\"username\":\"bobdb\",\"password\":\"bobdbPassword\"}");

        when(authenticationHelperService.checkUsernameCredentials("bobdb")).thenReturn(true);
        when(authenticationHelperService.checkEmailCredentials("bob.db@gmail.com")).thenReturn(true);

        mockMvc.perform(post("/api/public/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content(user.toString()))
                .andExpect(status().isOk());
    }

    @Test
    public void tryRegistrationWithUsernameThatIsAlreadyTakenTest() throws Exception{
        JSONObject user = new JSONObject("{\"firstName\":\"bob\",\"lastName\":\"de bouwer\",\"birthday\":\"1990-03-06\",\"gender\":\"Male\",\"email\":\"bob.db@gmail.com\",\"username\":\"bobdb\",\"password\":\"bobdbPassword\"}");

        when(authenticationHelperService.register(any(UserDto.class))).thenThrow(CustomAuthenticationException.class);

        mockMvc.perform(post("/api/public/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(user.toString()))
                .andExpect(status().isConflict());
    }

    @Test
    public void tryRegistrationWithEmailThatIsAlreadyTakenTest() throws Exception{
        JSONObject user = new JSONObject("{\"firstName\":\"bob\",\"lastName\":\"de bouwer\",\"birthday\":\"1990-03-06\",\"gender\":\"Male\",\"email\":\"bob.db@gmail.com\",\"username\":\"bobdb\",\"password\":\"bobdbPassword\"}");

        when(authenticationHelperService.register(any(UserDto.class))).thenThrow(CustomAuthenticationException.class);

        mockMvc.perform(post("/api/public/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(user.toString()))
                .andExpect(status().isConflict());
    }

    @Test
    public void tryRegistrationWithEmailAndUsernameThatAreAlreadyTakenTest() throws Exception{
        JSONObject user = new JSONObject("{\"firstName\":\"bob\",\"lastName\":\"de bouwer\",\"birthday\":\"1990-03-06\",\"gender\":\"Male\",\"email\":\"bob.db@gmail.com\",\"username\":\"bobdb\",\"password\":\"bobdbPassword\"}");

        when(authenticationHelperService.register(any(UserDto.class))).thenThrow(CustomAuthenticationException.class);

        mockMvc.perform(post("/api/public/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(user.toString()))
                .andExpect(status().isConflict());
    }
}
