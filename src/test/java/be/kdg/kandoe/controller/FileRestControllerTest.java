package be.kdg.kandoe.controller;

import be.kdg.kandoe.domain.user.Authority;
import be.kdg.kandoe.domain.user.Gender;
import be.kdg.kandoe.domain.user.User;
import be.kdg.kandoe.security.TokenHelper;
import be.kdg.kandoe.service.declaration.AuthenticationHelperService;
import be.kdg.kandoe.service.declaration.StorageService;
import be.kdg.kandoe.service.declaration.UserService;
import be.kdg.kandoe.service.implementation.CustomUserDetailsService;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Stream;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class FileRestControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private StorageService storageService;

    @MockBean
    private AuthenticationHelperService authenticationHelperService;

    @MockBean
    private UserService userService;

    @Autowired
    private TokenHelper tokenHelper;

    @MockBean
    private CustomUserDetailsService userDetailsService;

    private DeviceDummy deviceDummy;

    private User bob;

//    private final UserService userService;
//    private final AuthenticationHelperService authenticationHelperService;
//    private final StorageService storageService;
//    private final GameSessionService gameSessionService;

    @Before
    public void setup(){
        deviceDummy = new DeviceDummy();
        deviceDummy.setNormal(true);
        bob = new User("bob", "de bouwer", "bobdb", "bobdb@gmail.com", 6, 3, 1990, "bobdbPassword", Gender.Male, null);
        bob.setUserId(1);
        Authority bobRole = new Authority();
        bobRole.setName("ROLE_USER");
        bobRole.setUser(bob);
        bob.setAuthorities(Arrays.asList(bobRole));
    }


    private String createToken(String username, String roles){
        return tokenHelper.generateToken(username, deviceDummy, roles);
    }

//    @Test
//    public void uploadProfilePicture() throws Exception{
//
//        MockMultipartFile multipartFile = new MockMultipartFile("file", "image.txt",
//                "text/plain", "Spring Framework".getBytes());
//
//
//        MockHttpServletRequest request = new MockHttpServletRequest();
//        request.addHeader("Authorization" , "Bearer " + createToken(bob.getUsername(), "ROLE_USER"));
//
//
//        when(userService.findUserByUsername(bob.getUsername())).thenReturn(bob);
//        when(authenticationHelperService.userIsAllowedToAccessResource(request, bob.getUsername())).thenReturn(true);
//        when(userDetailsService.loadUserByUsername(bob.getUsername())).thenReturn(bob);
//
//
//        //verify(storageService, times(1)).store(multipartFile);
//
//
//        this.mvc.perform(fileUpload("/api/private/users/" + bob.getUsername() + "/uploadImage")
//        .file(multipartFile)
//        .header("Authorization", "Bearer " + createToken(bob.getUsername(), "ROLE_USER"))
//        .content(multipartFile.getBytes()))
//                .andExpect(status().isOk());
//
//    }

//    @Test
//    public void shouldListAllFiles() throws Exception {
//        given(this.storageService.loadAll())
//                .willReturn(Stream.of(Paths.get("first.txt"), Paths.get("second.txt")));
//
//        this.mvc.perform(get("/")).andExpect(status().isOk())
//                .andExpect(model().attribute("files",
//                        Matchers.contains("http://localhost/files/first.txt",
//                                "http://localhost/files/second.txt")));
//    }

//    @Test
//    public void shouldSaveUploadedFile() throws Exception {
//
//        MockMultipartFile multipartFile = new MockMultipartFile("file", "test.txt",
//                "text/plain", "Spring Framework".getBytes());
//        this.mvc.perform(fileUpload("/api/private/users/{username}/uploadImage").file(multipartFile))
//                .andExpect(status().isFound())
//                .andExpect(header().string("Location", "/"));
//
//        then(this.storageService).should().store(multipartFile);
//    }
//
//    @SuppressWarnings("unchecked")
//    @Test
//    public void should404WhenMissingFile() throws Exception {
//        given(this.storageService.loadAsResource("test.txt"))
//                .willThrow(StorageFileNotFoundException.class);
//
//        this.mvc.perform(get("/files/test.txt")).andExpect(status().isNotFound());
//    }
}
