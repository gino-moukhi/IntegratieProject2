package be.kdg.kandoe.service;

import be.kdg.kandoe.common.DeviceProvider;
import be.kdg.kandoe.domain.user.Gender;
import be.kdg.kandoe.domain.user.User;
import be.kdg.kandoe.dto.UserDto;
import be.kdg.kandoe.repository.declaration.UserRepository;
import be.kdg.kandoe.security.TokenHelper;
import be.kdg.kandoe.service.declaration.AuthenticationHelperService;
import be.kdg.kandoe.service.declaration.UserService;
import be.kdg.kandoe.service.exception.CustomAuthenticationException;
import be.kdg.kandoe.service.implementation.AuthenticationHelperServiceImpl;
import be.kdg.kandoe.service.implementation.CustomUserDetailsService;
import be.kdg.kandoe.service.implementation.UserServiceImpl;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.Calendar;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;
import static org.hamcrest.CoreMatchers.is;

@RunWith(MockitoJUnitRunner.class)
public class AuthenticationHelperServiceTest {

    private UserService userService;
//
    private UserRepository userRepository;
//
    private PasswordEncoder passwordEncoder;


    @Mock
    private TokenHelper tokenHelper;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private CustomUserDetailsService userDetailsService;

    @Mock
    private DeviceProvider deviceProvider;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Spy
    private AuthenticationHelperServiceImpl authenticationHelperService = new AuthenticationHelperServiceImpl(userService, tokenHelper, authenticationManager, userDetailsService, deviceProvider);

    @Before
    public void setup(){
        userRepository = mock(UserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userRepository, passwordEncoder);
        authenticationHelperService.setAuthenticationManager(authenticationManager);
        authenticationHelperService.setDeviceProvider(deviceProvider);
        authenticationHelperService.setTokenHelper(tokenHelper);
        authenticationHelperService.setUserDetailsService(userDetailsService);
        authenticationHelperService.setUserService(userService);

    }

    private boolean checkForUserDetails(User expectedUser, User userToCheck){
        boolean containsUsername = expectedUser.getUsername().equalsIgnoreCase(userToCheck.getUsername());
        boolean containsEmail = expectedUser.getEmail().equalsIgnoreCase(userToCheck.getEmail());
        boolean containsFirstName = expectedUser.getFirstName().equalsIgnoreCase(userToCheck.getFirstName());
        boolean containsLastName = expectedUser.getLastName().equalsIgnoreCase(userToCheck.getLastName());
        boolean containsDay = expectedUser.getDay() == userToCheck.getDay();
        boolean containsMonth = expectedUser.getMonth() == userToCheck.getMonth();
        boolean containsYear = expectedUser.getYear() == userToCheck.getYear();
        boolean containsGender = expectedUser.getGender() == userToCheck.getGender();
        boolean containsPassword = expectedUser.getPassword().equals(userToCheck.getPassword());
        boolean containsAuthorities = expectedUser.getAuthorities().containsAll(userToCheck.getAuthorities());
        boolean containsGameSessionInfos = expectedUser.getGameSessionInfos().containsAll(userToCheck.getGameSessionInfos());
        boolean containsProfilePicture = expectedUser.getProfilePictureFileName().equals(userToCheck.getProfilePictureFileName());


        if(containsUsername &&
                containsEmail &&
                containsFirstName &&
                containsLastName &&
                containsDay &&
                containsMonth &&
                containsYear &&
                containsGender &&
                containsPassword &&
                containsAuthorities &&
                containsGameSessionInfos &&
                containsProfilePicture) return true;

        return false;
    }

    @Test
    public void registerWithAUsedUsername() throws Exception{
        Calendar calendar = Calendar.getInstance();
        calendar.set(19982, 1, 3);
        UserDto userDto = new UserDto("bobdb", "kunnenWijHetMaken", "bob", "de bouwer", "bobdb@gmail.com", 36, Gender.Male, calendar);

        //false = already used
        //true = not used
        doReturn(false).when(authenticationHelperService).checkUsernameCredentials(userDto.getUsername());
        doReturn(true).when(authenticationHelperService).checkEmailCredentials(userDto.getEmail());
        expectedException.expect(CustomAuthenticationException.class);
        expectedException.expectMessage("Username is already used!");

        authenticationHelperService.register(userDto);

        verify(userDetailsService, never()).addUser(any(User.class));
    }

    @Test
    public void registerWithAUsedEmail() throws Exception{
        Calendar calendar = Calendar.getInstance();
        calendar.set(19982, 1, 3);
        UserDto userDto = new UserDto("bobdb", "kunnenWijHetMaken", "bob", "de bouwer", "bobdb@gmail.com", 36, Gender.Male, calendar);


        //false = already used
        //true = not used
        doReturn(true).when(authenticationHelperService).checkUsernameCredentials(userDto.getUsername());
        doReturn(false).when(authenticationHelperService).checkEmailCredentials(userDto.getEmail());
        expectedException.expect(CustomAuthenticationException.class);
        expectedException.expectMessage("Email is already used!");

        authenticationHelperService.register(userDto);

        verify(userDetailsService, never()).addUser(any(User.class));
    }

    @Test
    public void registerWithAUsedUsernameAndEmail() throws Exception{
        Calendar calendar = Calendar.getInstance();
        calendar.set(19982, 1, 3);
        UserDto userDto = new UserDto("bobdb", "kunnenWijHetMaken", "bob", "de bouwer", "bobdb@gmail.com", 36, Gender.Male, calendar);


        //false = already used
        //true = not used
        doReturn(false).when(authenticationHelperService).checkUsernameCredentials(userDto.getUsername());
        doReturn(false).when(authenticationHelperService).checkEmailCredentials(userDto.getEmail());
        expectedException.expect(CustomAuthenticationException.class);
        expectedException.expectMessage("Username and email already used!");

        authenticationHelperService.register(userDto);

        verify(userDetailsService, never()).addUser(any(User.class));
    }

    //TODO check verify
    @Test
    public void registerWithoutAUsedUsernameOrEmail() throws Exception{
        Calendar calendar = Calendar.getInstance();
        calendar.set(19982, 1, 3);
        UserDto userDto = new UserDto("bobdb", "kunnenWijHetMaken", "bob", "de bouwer", "bobdb@gmail.com", 36, Gender.Male, calendar);

        User user = new User(userDto);

        //false = already used
        //true = not used
        doReturn(true).when(authenticationHelperService).checkUsernameCredentials(userDto.getUsername());
        doReturn(true).when(authenticationHelperService).checkEmailCredentials(userDto.getEmail());

        when(passwordEncoder.encode(user.getEncryptedPassword())).thenReturn("encryptedPassword");
        when(userDetailsService.addUser(any(User.class))).thenReturn(user);
        when(userDetailsService.saveUser(any(User.class))).thenReturn(user);

        boolean result = authenticationHelperService.register(userDto);

        verify(userDetailsService, times(1)).addUser(any(User.class));
        //verify(userDetailsService, times(1)).saveUser(any(User.class));
        assertThat(result, is(true));
    }

    //TODO check verify
    @Test
    public void registerWithoutAUsedUsernameOrEmailCheckArguments(){
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

        Calendar calendar = Calendar.getInstance();
        calendar.set(19982, 1, 3);
        UserDto userDto = new UserDto("bobdb", "kunnenWijHetMaken", "bob", "de bouwer", "bobdb@gmail.com", 36, Gender.Male, calendar);

        User user = new User(userDto);

        //false = already used
        //true = not used
        doReturn(true).when(authenticationHelperService).checkUsernameCredentials(userDto.getUsername());
        doReturn(true).when(authenticationHelperService).checkEmailCredentials(userDto.getEmail());

        when(passwordEncoder.encode(user.getEncryptedPassword())).thenReturn("encryptedPassword");
        when(userDetailsService.addUser(any(User.class))).thenReturn(user);
        when(userDetailsService.saveUser(any(User.class))).thenReturn(user);

        boolean result = authenticationHelperService.register(userDto);

        verify(userDetailsService).addUser(userArgumentCaptor.capture());
        assertThat(true, is(checkForUserDetails(user, userArgumentCaptor.getValue())));

        //verify(userDetailsService).saveUser(userArgumentCaptor.capture());
        //assertThat(true, is(checkForUserDetails(user, userArgumentCaptor.getValue())));
    }

    //TODO add tests
}
