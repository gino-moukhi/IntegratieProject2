package be.kdg.kandoe.integration;

import be.kdg.kandoe.KandoeApplication;
import be.kdg.kandoe.controller.rest.ThemeRestController;
import be.kdg.kandoe.domain.theme.SubTheme;
import be.kdg.kandoe.domain.theme.Theme;
import be.kdg.kandoe.dto.SubThemeDto;
import be.kdg.kandoe.dto.ThemeDto;
import be.kdg.kandoe.service.implementation.ThemeServiceImpl;
import be.kdg.kandoe.unit.theme.ThemeRepoMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
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
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest
public class TestThemeRestController {

    static ThemeRestController controller;

    static ThemeDto theme1;
    static ThemeDto theme2;

    static SubThemeDto subTheme1;
    static SubThemeDto subTheme2;

    private static TestRestTemplate restTemplate = new TestRestTemplate();

    @Before
    public void init(){
        controller = new ThemeRestController(new ThemeServiceImpl(new ThemeRepoMock()));
        theme1= new ThemeDto(0L,"School","TestTheme Everything to do with school");
        theme2= new ThemeDto(0L, "Industry","TestTheme Everything to do with Industry");

        subTheme1= new SubThemeDto(0L,theme1,"Extra classroom","Build extra classrooms to expand student limit");
        subTheme2=new SubThemeDto(0L,theme1,"More recreation","Allow students to relax more while on break");
        setupDb();
    }

    @Test
    public void TestCreateTheme(){
        ThemeDto themeDto = new ThemeDto(0L,"JSONTheme","Theme created via JSON");
        ResponseEntity<ThemeDto> response = restTemplate.postForEntity("http://localhost:9090/api/public/themes", themeDto, ThemeDto.class);
        Assert.assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        Assert.assertThat(response.getBody().getClass(),equalTo(ThemeDto.class));
        Assert.assertThat(response.getBody().getName(),equalTo(themeDto.getName()));
        setupDb();
    }

    @Test
    public void TestCreateSubTheme(){
        /*SubThemeDto subThemeDto = new SubThemeDto(0L, null,"AddedSubTheme","This subtheme is added by Tests");
        ResponseEntity<SubThemeDto> response = restTemplate.postForEntity("http://localhost:9090/api/public/subthemes/1", subThemeDto, SubThemeDto.class);
        Assert.assertThat(response.getStatusCode(),equalTo(HttpStatus.OK));
        Assert.assertThat(response.getBody().getTheme().getThemeId(),equalTo(theme1.getThemeId()));
        Assert.assertThat(response.getBody().getSubThemeName(),equalTo(subThemeDto.getSubThemeName()));*/
    }

    @Test
    public void TestGetAllThemes(){
        ParameterizedTypeReference<List<ThemeDto>> typeref = new ParameterizedTypeReference<List<ThemeDto>>() {
        };
        List<ThemeDto> themeDtos =  restTemplate.exchange("http://localhost:9090/api/public/themes", HttpMethod.GET,null,typeref).getBody();
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
        ResponseEntity<Theme> response1=restTemplate.getForEntity("http://localhost:9090/api/public/theme/"+theme1.getThemeId(),Theme.class);
        Assert.assertThat(response1.getStatusCode(),equalTo(HttpStatus.OK));
        Assert.assertThat(response1.getBody().getClass(), equalTo(Theme.class));
        Assert.assertThat(response1.getBody().getName(),equalTo(theme1.getName()));
        System.out.println(response1.getBody().getName());
        ResponseEntity<Theme> response2=restTemplate.getForEntity("http://localhost:9090/api/public/theme/"+theme2.getThemeId(),Theme.class);
        Assert.assertThat(response2.getStatusCode(),equalTo(HttpStatus.OK));
        Assert.assertThat(response2.getBody().getClass(), equalTo(Theme.class));
        Assert.assertThat(response2.getBody().getName(),equalTo(theme2.getName()));
        System.out.println(response2.getBody().getName());
    }

    @Test
    public void TestGetThemeByName(){
        ResponseEntity<ThemeDto> response=restTemplate.getForEntity("http://localhost:9090/api/public/theme?name=School",ThemeDto.class);
        Assert.assertThat(response.getStatusCode(),equalTo(HttpStatus.OK));
        Assert.assertThat(response.getBody().getClass(),equalTo(ThemeDto.class));
        Assert.assertThat(response.getBody().getDescription(),equalTo(theme1.getDescription()));
    }

    @Test
    public void TestGetSubThemeById(){
        /*
        ResponseEntity<SubThemeDto> response = restTemplate.getForEntity("http://localhost:9090/api/public/subtheme/"+subTheme1.getSubThemeId(),SubThemeDto.class);
        Assert.assertThat(response.getStatusCode(),equalTo(HttpStatus.OK));
        Assert.assertThat(response.getBody().getSubThemeId(),equalTo(new Long(1)));
        Assert.assertThat(response.getBody().getSubThemeName(),equalTo(subTheme1.getSubThemeName()));
        */
    }

    @Test
    public void TestGetSubThemesByThemeId(){
        ResponseEntity<SubThemeDto> response;
    }

    @Test
    public void TestEditTheme(){
        ThemeDto themeDto = restTemplate.getForEntity("http://localhost:9090/api/public/theme/"+theme1.getThemeId(),ThemeDto.class).getBody();
        themeDto.setName("SchoolUpdated");
        themeDto.setDescription("Updated Theme of School");
        HttpEntity<ThemeDto> httpEntity = new HttpEntity<ThemeDto>(themeDto);
        ResponseEntity<ThemeDto> response = restTemplate.exchange("http://localhost:9090/api/public/theme/"+theme1.getThemeId(),HttpMethod.PUT,httpEntity,ThemeDto.class);
        Assert.assertThat(response.getStatusCode(),equalTo(HttpStatus.OK));
        Assert.assertThat(response.getBody().getName(),not(theme1.getName()));
        Assert.assertThat(response.getBody().getDescription(),not(theme1.getDescription()));
        Assert.assertThat(response.getBody().getThemeId(),equalTo(themeDto.getThemeId()));
    }

    @Test
    public void TestEditSubTheme(){
        /*
        SubThemeDto subThemeDto = restTemplate.getForEntity("http://localhost:9090/api/public/subtheme/"+subTheme1.getSubThemeId(),SubThemeDto.class).getBody();
        subThemeDto.setSubThemeName("Updated SubTheme");
        subThemeDto.setSubThemeDescription("Updated Subtheme should not be equal to the old version");
        subThemeDto.setTheme(theme2);
        HttpEntity<SubThemeDto> httpEntity = new HttpEntity<>(subThemeDto);
        ResponseEntity<SubThemeDto> response = restTemplate.exchange("http://localhost:9090/api/public/subtheme/"+theme2.getThemeId(),HttpMethod.PUT,httpEntity,SubThemeDto.class);

        Assert.assertThat(response.getStatusCode(),equalTo(HttpStatus.OK));
        Assert.assertThat(response.getBody().getSubThemeName(),not(subTheme1.getSubThemeName()));
        Assert.assertThat(response.getBody().getSubThemeDescription(),not(subTheme1.getSubThemeDescription()));
        Assert.assertThat(response.getBody().getTheme().getThemeId(),equalTo(subTheme1.getTheme().getThemeId()));
        */
    }


    @Test
    public void TestRemoveNonExistingTheme(){
        ResponseEntity<Theme> response = restTemplate.exchange("http://localhost:9090/api/public/theme/"+theme2.getThemeId()+5,HttpMethod.DELETE,null,Theme.class);
        Assert.assertThat(response.getStatusCode(),equalTo(HttpStatus.NOT_FOUND));
    }

    @Test
    public void TestRemoveThemeById(){
        ResponseEntity<Theme> responseDelete = restTemplate.exchange("http://localhost:9090/api/public/theme/"+theme2.getThemeId(), HttpMethod.DELETE,null,Theme.class);
        ResponseEntity<Theme> responseGet = restTemplate.getForEntity("http://localhost:9090/api/public/theme/"+theme2.getThemeId(),Theme.class);
        Assert.assertThat(responseDelete.getStatusCode(),equalTo(HttpStatus.OK));
        Assert.assertThat(responseGet.getStatusCode(),equalTo(HttpStatus.NOT_FOUND));
        Assert.assertThat("Found theme should be NULL", responseGet.getStatusCode(),equalTo(HttpStatus.NOT_FOUND));
    }

    @Test
    public void TestRemoveSubThemesByThemeId(){
        /*ParameterizedTypeReference<List<SubThemeDto>> typeref = new ParameterizedTypeReference<List<SubThemeDto>>() {
        };
        ResponseEntity<List<SubThemeDto>> responseDelete =  restTemplate.exchange("http://localhost:9090/api/public/subthemes/1", HttpMethod.DELETE,null,typeref);
        Assert.assertThat(responseDelete.getStatusCode(),equalTo(HttpStatus.OK));
        Assert.assertThat(responseDelete.getBody().size(),equalTo(2));*/
    }
    @Test
    public void TestGetThemeNonExistent(){
        ResponseEntity<Theme> responseGet = restTemplate.getForEntity("http://localhost:9090/api/public/theme/"+theme2.getThemeId()+7,Theme.class);
        Assert.assertThat(responseGet.getStatusCode(),equalTo(HttpStatus.NOT_FOUND));
    }

    private void setupDb(){
        ResponseEntity<String> response= restTemplate.exchange("http://localhost:9090/api/public/themes",HttpMethod.DELETE,null,String.class);
        ResponseEntity<ThemeDto> response1=restTemplate.postForEntity("http://localhost:9090/api/public/themes", theme1, ThemeDto.class);
        theme1=response1.getBody();
        ResponseEntity<ThemeDto> response2=restTemplate.postForEntity("http://localhost:9090/api/public/themes", theme2, ThemeDto.class);
        theme2=response2.getBody();
        /*ResponseEntity<SubThemeDto> response3=restTemplate.postForEntity("http://localhost:9090/api/public/subthemes/1",subTheme1,SubThemeDto.class);
        subTheme1=response3.getBody();
        ResponseEntity<SubThemeDto> response4=restTemplate.postForEntity("http://localhost:9090/api/public/subthemes/1",subTheme2,SubThemeDto.class);
        subTheme2=response4.getBody();*/
    }
}