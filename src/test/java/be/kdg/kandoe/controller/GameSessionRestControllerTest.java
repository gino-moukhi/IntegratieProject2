package be.kdg.kandoe.controller;

import be.kdg.kandoe.domain.GameSession;
import be.kdg.kandoe.domain.user.Authority;
import be.kdg.kandoe.domain.user.Gender;
import be.kdg.kandoe.domain.user.User;
import be.kdg.kandoe.dto.gameSession.CreateGameSessionDto;
import be.kdg.kandoe.security.TokenHelper;
import be.kdg.kandoe.service.declaration.GameSessionService;
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
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@EnableWebSecurity
public class GameSessionRestControllerTest {
    private MockMvc mockMvc;

    @Autowired
    protected WebApplicationContext wac;

    @MockBean
    private UserService userService;

    @MockBean
    private GameSessionService gameSessionService;

    @Autowired
    private TokenHelper tokenHelper;

    private DeviceDummy deviceDummy;

    @MockBean
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




        gameSessions = new ArrayList<>();
        CreateGameSessionDto createGameSessionDto = new CreateGameSessionDto("test session", bob.getUsername(), true, true, 3, 4, 3600);
        GameSession gameSession = new GameSession(createGameSessionDto, bob);

        CreateGameSessionDto createGameSessionDto2 = new CreateGameSessionDto("test session 2", bob.getUsername(), false, true, 3, 4, 3600);
        GameSession gameSession2 = new GameSession(createGameSessionDto2, bob);

        CreateGameSessionDto createGameSessionDto3 = new CreateGameSessionDto("test session", mindy.getUsername(), true, false, 3, 4, 3600);
        GameSession gameSession3 = new GameSession(createGameSessionDto3, mindy);

        CreateGameSessionDto createGameSessionDto4 = new CreateGameSessionDto("test session", mindy.getUsername(), false, false, 3, 4, 3600);
        GameSession gameSession4 = new GameSession(createGameSessionDto, mindy);


        gameSessions.add(gameSession);
        gameSessions.add(gameSession2);
        gameSessions.add(gameSession3);
        gameSessions.add(gameSession4);

    }

    private boolean checkForGameSessionDetails(String json, GameSession gameSession){
        boolean containsTitle = json.contains(gameSession.getTitle());
        boolean containsImage = json.contains(gameSession.getImage());
        boolean containsOrganisatorName = json.contains(gameSession.getOrganisatorName());
        boolean containsTimerLength = json.contains(String.valueOf(gameSession.getTimerLength()));
        boolean containsSelectionLimit = json.contains(String.valueOf(gameSession.getSelectionLimit()));
        boolean containsAddLimit = json.contains(String.valueOf(gameSession.getAddLimit()));
        boolean containsGameSessionId = json.contains(String.valueOf(gameSession.getGameSessionId()));
        //todo GameSessionInfo?

        if(containsTitle &&
                containsImage &&
                containsOrganisatorName &&
                containsTimerLength &&
                containsSelectionLimit &&
                containsAddLimit &&
                containsGameSessionId) return true;
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
    public void createGameSessionWithAnonymousAccount() throws Exception{
        this.mockMvc.perform(post("/api/private/sessions"))
                .andExpect(status().isUnauthorized());
    }

//    @Test
//    @WithMockUser(roles = "ADMIN")
//    public void createGameSessionWithAdminAccount() throws Exception{
//        CreateGameSessionDto dto = new CreateGameSessionDto("My new session", sven.getUsername(), true, true, 3, 4, 8000);
//
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("title", "My new session");
//        jsonObject.put("organisator", sven.getUsername());
//        jsonObject.put("isOrganisatorPlaying", true);
//        jsonObject.put("allowUsersToAdd", true);
//        jsonObject.put("limit", 3);
//        jsonObject.put("selectionLimit", 4);
//        jsonObject.put("timer", 8000);
//
//        GameSession expectedGameSession = new GameSession(dto, mindy);
//
//        String jwtToken = createToken(sven.getUsername(), "ROLE_ADMIN");
//
//        when(userService.findUserByUsername(sven.getUsername())).thenReturn(sven);
//        when(userDetailsService.loadUserByUsername(sven.getUsername())).thenReturn(sven);
//        when(gameSessionService.addGameSession(expectedGameSession)).thenReturn(expectedGameSession);
//
//        MvcResult result = this.mockMvc.perform(post("/api/private/sessions")
//                .header("Authorization", "Bearer " + jwtToken)
//                .content(jsonObject.toString())
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andReturn();
//        String jsonResponse = result.getResponse().getContentAsString();
//        assertThat(true, is(checkForGameSessionDetails(jsonResponse, expectedGameSession)));
//    }



}
