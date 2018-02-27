package be.kdg.kandoe.integration;

import be.kdg.kandoe.controller.rest.ThemeRestController;
import be.kdg.kandoe.domain.theme.Theme;
import be.kdg.kandoe.dto.ThemeDto;
import be.kdg.kandoe.service.implementation.ThemeServiceImpl;
import be.kdg.kandoe.unit.theme.ThemeRepoMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.IsEqual.equalTo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TestThemeRestController {
    static ThemeRestController controller;

    static ThemeDto theme1;
    static ThemeDto theme2;

    private static TestRestTemplate restTemplate = new TestRestTemplate();

    @Before
    public void init(){
        controller = new ThemeRestController(new ThemeServiceImpl(new ThemeRepoMock()));
        theme1= new ThemeDto(0,"School","TestTheme Everything to do with school");
        theme2= new ThemeDto(0, "Industry","TestTheme Everything to do with Industry");
        setupDb();
    }

    @Test
    public void TestCreateTheme(){
        ThemeDto themeDto = new ThemeDto(3,"JSONTheme","Theme created via JSON");
        ResponseEntity<ThemeDto> response = restTemplate.postForEntity("http://localhost:9090/api/themes", themeDto, ThemeDto.class);
        Assert.assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        Assert.assertThat(response.getBody().getClass(),equalTo(ThemeDto.class));
        setupDb();
    }

    @Test
    public void TestGetAllThemes(){
        ParameterizedTypeReference<List<ThemeDto>> typeref = new ParameterizedTypeReference<List<ThemeDto>>() {
        };
        List<ThemeDto> themeDtos =  restTemplate.exchange("http://localhost:9090/api/themes", HttpMethod.GET,null,typeref).getBody();
        for (ThemeDto t:themeDtos
                ) {
            System.out.println(t.getName()+" - "+t.getDescription());
        }
        Assert.assertNotNull(themeDtos);
        Assert.assertThat(themeDtos.size(),equalTo(2));
        setupDb();
    }

    @Test
    public void TestGetThemeById(){

        ResponseEntity<Theme> response1=restTemplate.getForEntity("http://localhost:9090/api/theme/1",Theme.class);
        Assert.assertThat(response1.getStatusCode(),equalTo(HttpStatus.OK));
        Assert.assertThat(response1.getBody().getClass(), equalTo(Theme.class));
        Assert.assertThat(response1.getBody().getName(),equalTo(theme1.getName()));
        System.out.println(response1.getBody().getName());
        ResponseEntity<Theme> response2=restTemplate.getForEntity("http://localhost:9090/api/theme/2",Theme.class);
        Assert.assertThat(response2.getStatusCode(),equalTo(HttpStatus.OK));
        Assert.assertThat(response2.getBody().getClass(), equalTo(Theme.class));
        Assert.assertThat(response2.getBody().getName(),equalTo(theme2.getName()));
        System.out.println(response2.getBody().getName());
    }

    @Test
    public void TestGetThemeByName(){
        ResponseEntity<Theme> response=restTemplate.getForEntity("http://localhost:9090/api/theme?name=School",Theme.class);
        Assert.assertThat(response.getStatusCode(),equalTo(HttpStatus.OK));
        Assert.assertThat(response.getBody().getClass(),equalTo(Theme.class));
        Assert.assertThat(response.getBody().getDescription(),equalTo(theme1.getDescription()));
    }

    @Test
    public void TestEditTheme(){
        ThemeDto themeDto = restTemplate.getForEntity("http://localhost:9090/api/theme/1",ThemeDto.class).getBody();
        themeDto.setName("SchoolUpdated");
        themeDto.setDescription("Updated Theme of School");
        HttpEntity<ThemeDto> httpEntity = new HttpEntity<ThemeDto>(themeDto);
        ResponseEntity<ThemeDto> response = restTemplate.exchange("http://localhost:9090/api/theme/1",HttpMethod.PUT,httpEntity,ThemeDto.class);

        Assert.assertThat(response.getStatusCode(),equalTo(HttpStatus.OK));
        Assert.assertThat(response.getBody().getName(),not(theme1.getName()));
        Assert.assertThat(response.getBody().getDescription(),not(theme1.getDescription()));
        Assert.assertThat(response.getBody().getThemeId(),equalTo(themeDto.getThemeId()));
    }

    @Test
    public void TestRemoveTheme(){
        ResponseEntity<Theme> response = restTemplate.exchange("http://localhost:9090/api/theme/1", HttpMethod.DELETE,null,Theme.class);
        Assert.assertNotNull(response.getBody());
        Assert.assertThat(response.getBody().getClass(),equalTo(Theme.class));
        Assert.assertThat(response.getBody().getName(),equalTo("School"));
    }

    @Test
    public void TestRemoveNonExistingTheme(){
        ResponseEntity<Theme> response = restTemplate.exchange("http://localhost:9090/api/theme/5",HttpMethod.DELETE,null,Theme.class);
        Assert.assertThat(response.getStatusCode(),equalTo(HttpStatus.NOT_FOUND));
    }

    @Test
    public void TestRemoveThemeById(){
        ResponseEntity<Theme> responseDelete = restTemplate.exchange("http://localhost:9090/api/theme/2", HttpMethod.DELETE,null,Theme.class);
        ResponseEntity<Theme> responseGet = restTemplate.getForEntity("http://localhost:9090/api/theme/2",Theme.class);
        Assert.assertThat("Found theme should be NULL", responseGet.getStatusCode(),equalTo(HttpStatus.NOT_FOUND));
    }
    @Test
    public void TestGetThemeNonExistent(){
        ResponseEntity<Theme> responseDelete = restTemplate.exchange("http://localhost:9090/api/theme/2", HttpMethod.DELETE,null,Theme.class);
        ResponseEntity<Theme> responseGet = restTemplate.getForEntity("http://localhost:9090/api/theme/2",Theme.class);
        Assert.assertThat(responseDelete.getStatusCode(),equalTo(HttpStatus.OK));
        Assert.assertThat(responseGet.getStatusCode(),equalTo(HttpStatus.NOT_FOUND));
    }

    private void setupDb(){
        restTemplate.exchange("http://localhost:9090/api/themes",HttpMethod.DELETE,null,String.class);
        restTemplate.postForEntity("http://localhost:9090/api/themes", theme1, ThemeDto.class);
        restTemplate.postForEntity("http://localhost:9090/api/themes", theme2, ThemeDto.class);
    }
}