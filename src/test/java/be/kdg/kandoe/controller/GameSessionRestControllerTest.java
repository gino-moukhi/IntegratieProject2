package be.kdg.kandoe.controller;

import be.kdg.kandoe.domain.GameSession;
import be.kdg.kandoe.domain.GameSessionRole;
import be.kdg.kandoe.domain.Notification;
import be.kdg.kandoe.domain.user.Authority;
import be.kdg.kandoe.domain.user.Gender;
import be.kdg.kandoe.domain.user.User;
import be.kdg.kandoe.dto.gameSession.GameSessionDto;
import be.kdg.kandoe.dto.gameSession.NotificationDto;
import be.kdg.kandoe.security.TokenHelper;
import be.kdg.kandoe.service.declaration.GameSessionService;
import be.kdg.kandoe.service.declaration.UserService;
import be.kdg.kandoe.service.implementation.CustomUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@EnableWebSecurity
@Rollback
@Transactional
public class GameSessionRestControllerTest {
    private MockMvc mockMvc;

    @Autowired
    protected WebApplicationContext wac;

    @MockBean(name="userService2")
    private UserService userService;

    @MockBean(name="gameSessionService1")
    private GameSessionService gameSessionService;

    @Autowired
    private TokenHelper tokenHelper;

    @Autowired
    private ObjectMapper objectMapper;

    private DeviceDummy deviceDummy;

    @MockBean(name="userDetailService")
    private CustomUserDetailsService userDetailsService;

    private User bob;
    private User mindy;
    private User sven;

    private List<GameSession> gameSessions;


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

        userDetailsService.addUser(sven);


/*        try {
            mockMvc.perform(post("/api/private/users/"+sven.getUsername())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(sven.toString()))
                    .andExpect(status().isOk());
            mockMvc.perform(post("/api/private/users/"+mindy.getUsername())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mindy.toString()))
                    .andExpect(status().isOk());
            mockMvc.perform(post("/api/private/users/"+bob.getUsername())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(bob.toString()))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
*/
        List<Notification> allNotifications = new ArrayList<>();
        allNotifications.add(Notification.StartGame);
        allNotifications.add(Notification.EndGame);
        allNotifications.add(Notification.YourTurn);
        allNotifications.add(Notification.EndTurn);

        List<Notification> someEnabledNotifications = new ArrayList<>();
        someEnabledNotifications.add(Notification.YourTurn);
        someEnabledNotifications.add(Notification.EndGame);



        gameSessions = new ArrayList<>();
        GameSessionDto gameSessionDto = new GameSessionDto("test session", bob.getUsername(), true, true, 3, 4, 3600);
        //Only contains the creator (bob)
        GameSession gameSession = new GameSession(gameSessionDto, bob);
        gameSession.setGameSessionId(1l);


        GameSessionDto gameSessionDto2 = new GameSessionDto("test session 2", bob.getUsername(), false, true, 3, 4, 3600);
        //Contains bob and mindy
        GameSession gameSession2 = new GameSession(gameSessionDto2, bob);
        gameSession2.setGameSessionId(2l);
//        gameSession2.addUserToGameSession(new UserGameSessionInfo(someEnabledNotifications, false, GameSessionRole.Participant, mindy, gameSession));


        GameSessionDto gameSessionDto3 = new GameSessionDto("test session", mindy.getUsername(), true, false, 3, 4, 3600);
        //Contains mindy and sven
        GameSession gameSession3 = new GameSession(gameSessionDto3, mindy);
        gameSession3.setGameSessionId(3l);
//        gameSession3.addUserToGameSession(new UserGameSessionInfo(allNotifications, false, GameSessionRole.Participant, sven, gameSession));



        GameSessionDto gameSessionDto4 = new GameSessionDto("test session", mindy.getUsername(), false, false, 3, 4, 3600);
        //only mindy
        GameSession gameSession4 = new GameSession(gameSessionDto, mindy);
        gameSession4.setGameSessionId(4l);
//        gameSession4.addUserToGameSession(new UserGameSessionInfo(allNotifications, false, GameSessionRole.Moderator, bob, gameSession));






        gameSessions.add(gameSession);
        gameSessions.add(gameSession2);
        gameSessions.add(gameSession3);
        gameSessions.add(gameSession4);

    }

    private boolean checkForGameSessionDetails(String json, GameSession gameSession){
        boolean containsTitle = json.contains(gameSession.getTitle());
        boolean containsImage = json.contains(gameSession.getImage());
//        boolean containsOrganisatorName = json.contains(gameSession.getOrganisatorName());
        boolean containsTimerLength = json.contains(String.valueOf(gameSession.getTimerLength()));
        boolean containsSelectionLimit = json.contains(String.valueOf(gameSession.getSelectionLimit()));
        boolean containsAddLimit = json.contains(String.valueOf(gameSession.getAddLimit()));
        boolean containsGameSessionId = json.contains(String.valueOf(gameSession.getGameSessionId()));
        //todo GameSessionInfo?

        if(containsTitle &&
                containsImage &&
//                containsOrganisatorName &&
                containsTimerLength &&
                containsSelectionLimit &&
                containsAddLimit &&
                containsGameSessionId) return true;
        return false;
    }

    private boolean checkForuserDetails(String json, User user, GameSessionRole expectedRole){
        boolean containsUsername = json.contains(user.getUsername());
        boolean containsFirstName = json.contains(user.getFirstName());
        boolean containsLastName = json.contains(user.getLastName());
        boolean containsEmail = json.contains(user.getEmail());
        boolean containsGender = json.contains(String.valueOf(user.getGender()));
        boolean containsRole = json.contains(expectedRole.name());

        if(containsUsername &&
                containsFirstName &&
                containsLastName &&
                containsEmail &&
                containsGender &&
                containsRole) return true;
        return false;
    }

    private boolean checkForNotificationDetails(String json, NotificationDto expected){
        boolean containsStartGame = json.contains(String.valueOf(expected.startGame));
        boolean containsEndGame = json.contains(String.valueOf(expected.endGame));;
        boolean containsYourTurn = json.contains(String.valueOf(expected.yourTurn));;
        boolean containsEndTurn = json.contains(String.valueOf(expected.endTurn));;

        if(containsStartGame &&
                containsEndGame &&
                containsYourTurn &&
                containsEndTurn) return true;
        return false;
    }

    private String createToken(String username, String roles){
        return tokenHelper.generateToken(username, deviceDummy, roles);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void getAllSessionsWithAdminAccount() throws Exception{
        when(userDetailsService.loadUserByUsername("sven")).thenReturn(sven);
        when(userService.findUserByUsername("sven")).thenReturn(sven);
        when(gameSessionService.getAllGameSessions()).thenReturn(gameSessions);

        String jwtToken = createToken("sven", "ROLE_ADMIN");


        MvcResult result = mockMvc.perform(get("/api/private/gamesessions")
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + jwtToken))
                                .andExpect(status().isOk())
                                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        assertThat(true, is(checkForGameSessionDetails(jsonResponse, gameSessions.get(0))));
        assertThat(true, is(checkForGameSessionDetails(jsonResponse, gameSessions.get(1))));
        assertThat(true, is(checkForGameSessionDetails(jsonResponse, gameSessions.get(2))));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void getAllSessionsWithUserAccount() throws Exception{
        //Should be forbidden since only admins should be allowed to access this resource

        when(userDetailsService.loadUserByUsername(bob.getUsername())).thenReturn(bob);
        when(userService.findUserByUsername(bob.getUsername())).thenReturn(bob);
        when(gameSessionService.getAllGameSessions()).thenReturn(gameSessions);

        String jwtToken = createToken(bob.getUsername(), "ROLE_USER");

        mockMvc.perform(get("/api/private/gamesessions")
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    public void getAllSessionsWithAnonymousUser() throws Exception{
        mockMvc.perform(get("/api/private/gamesessions")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }



    @Test
    @WithAnonymousUser
    public void createGameSessionWithAnonymousAccount() throws Exception{
        this.mockMvc.perform(post("/api/private/sessions"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void createGameSessionWithAdminAccount() throws Exception{
        GameSessionDto dto = new GameSessionDto("My new session", sven.getUsername(), true, true, 3, 4, 8000);
        GameSession expectedGameSession = new GameSession(dto, mindy);
        expectedGameSession.setGameSessionId(1l);

        String jwtToken = createToken(sven.getUsername(), "ROLE_ADMIN");

        when(userService.findUserByUsername(dto.getOrganisator())).thenReturn(sven);
        when(userDetailsService.loadUserByUsername(sven.getUsername())).thenReturn(sven);
        when(gameSessionService.addGameSession(any(GameSession.class))).thenReturn(expectedGameSession);

        MvcResult result = this.mockMvc.perform(post("/api/private/sessions")
                .header("Authorization", "Bearer " + jwtToken)
                .content(objectMapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResult = result.getResponse().getContentAsString();
        assertThat(jsonResult,  containsString("1"));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void createGameSessionWithUserAccount() throws Exception{
        GameSessionDto dto = new GameSessionDto("My new session", bob.getUsername(), true, true, 3, 4, 8000);
        GameSession expectedGameSession = new GameSession(dto, bob);
        expectedGameSession.setGameSessionId(1l);

        String jwtToken = createToken(bob.getUsername(), "ROLE_USER");

        when(userService.findUserByUsername(dto.getOrganisator())).thenReturn(bob);
        when(userDetailsService.loadUserByUsername(bob.getUsername())).thenReturn(bob);
        when(gameSessionService.addGameSession(any(GameSession.class))).thenReturn(expectedGameSession);

        MvcResult result = this.mockMvc.perform(post("/api/private/sessions")
                .header("Authorization", "Bearer " + jwtToken)
                .content(objectMapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResult = result.getResponse().getContentAsString();
        assertThat(jsonResult,  containsString("1"));
    }



    @Test
    @WithMockUser(roles = "USER")
    public void getUsersFromSessionsWithUserAccount() throws Exception{

        when(userDetailsService.loadUserByUsername(mindy.getUsername())).thenReturn(mindy);
        when(gameSessionService.getGameSessionWithId(gameSessions.get(2).getGameSessionId())).thenReturn(gameSessions.get(2));

        String jwtToken = createToken(mindy.getUsername(), "ROLE_USER");
        MvcResult result = mockMvc.perform(
                get("/api/private/sessions/" + gameSessions.get(2).getGameSessionId() + "/users")
                .header("Authorization", "Bearer " + jwtToken)
        )       .andExpect(status().isOk())
                .andReturn();
        String jsonResponse = result.getResponse().getContentAsString();
        assertThat(true, is(checkForuserDetails(jsonResponse, mindy, GameSessionRole.ModeratorParticipant)));
        assertThat(true, is(checkForuserDetails(jsonResponse, sven, GameSessionRole.Participant)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void getUsersFromSessionsWithAdminAccount() throws Exception{

        when(userDetailsService.loadUserByUsername(sven.getUsername())).thenReturn(sven);
        when(gameSessionService.getGameSessionWithId(gameSessions.get(2).getGameSessionId())).thenReturn(gameSessions.get(2));

        String jwtToken = createToken(sven.getUsername(), "ROLE_ADMIN");
        MvcResult result = mockMvc.perform(
                get("/api/private/sessions/" + gameSessions.get(2).getGameSessionId() + "/users")
                        .header("Authorization", "Bearer " + jwtToken)
        )       .andExpect(status().isOk())
                .andReturn();
        String jsonResponse = result.getResponse().getContentAsString();
        assertThat(true, is(checkForuserDetails(jsonResponse, mindy, GameSessionRole.ModeratorParticipant)));
        assertThat(true, is(checkForuserDetails(jsonResponse, sven, GameSessionRole.Participant)));
    }

    @Test
    @WithAnonymousUser
    public void getUsersFromSessionWithAnonymousAccount() throws Exception{
        mockMvc.perform(
                get("/api/private/sessions/" + gameSessions.get(2).getGameSessionId() + "/users")
        )       .andExpect(status().isUnauthorized());
    }



    @Test
    @WithMockUser(roles = "USER")
    public void getNotificationsSettingsOfUserFromGameSessionWithUserAccount() throws Exception{
        when(userDetailsService.loadUserByUsername(mindy.getUsername())).thenReturn(mindy);
        when(userService.findUserByUsername(mindy.getUsername())).thenReturn(mindy);
        when(gameSessionService.getGameSessionWithId(gameSessions.get(1).getGameSessionId())).thenReturn(gameSessions.get(1));


        //when(authenticationHelperService.userIsAllowedToAccessResource())


        NotificationDto expected = new NotificationDto(false, true, true, false);
        String jwtToken = createToken(mindy.getUsername(), "ROLE_USER");


        MvcResult result = mockMvc.perform(
                get("/api/private/users/" + mindy.getUsername() + "/sessions/" + gameSessions.get(1).getGameSessionId() + "/notifications")
                .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andReturn();
        String jsonResult = result.getResponse().getContentAsString();
        assertThat(true, is(checkForNotificationDetails(jsonResult, expected)));

    }


    @Test
    @WithMockUser(roles = "USER")
    public void getNotificationsSettingsOfAnotherUserFromGameSessionWithUserAccount() throws Exception{

        //trying to get bobs notification settings with mindy her credentials
        //should be unauthorized
        when(userDetailsService.loadUserByUsername(mindy.getUsername())).thenReturn(mindy);
        when(userService.findUserByUsername(mindy.getUsername())).thenReturn(mindy);
        when(gameSessionService.getGameSessionWithId(gameSessions.get(1).getGameSessionId())).thenReturn(gameSessions.get(1));

        NotificationDto expected = new NotificationDto(false, true, true, false);
        String jwtToken = createToken(mindy.getUsername(), "ROLE_USER");


        MvcResult result = mockMvc.perform(
                get("/api/private/users/" + bob.getUsername() + "/sessions/" + gameSessions.get(1).getGameSessionId() + "/notifications")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

}
