package be.kdg.kandoe.integration;

import be.kdg.kandoe.controller.rest.ThemeRestController;
import be.kdg.kandoe.domain.theme.Card;
import be.kdg.kandoe.domain.theme.SubTheme;
import be.kdg.kandoe.domain.theme.Theme;
import be.kdg.kandoe.dto.converter.DtoConverter;
import be.kdg.kandoe.dto.theme.CardDto;
import be.kdg.kandoe.dto.theme.CardSubThemeDto;
import be.kdg.kandoe.dto.theme.SubThemeDto;
import be.kdg.kandoe.dto.theme.ThemeDto;
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
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.IsEqual.equalTo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TestThemeRestController {
    static ThemeRestController controller;

    static String callURL = "http://localhost:9090/";

    static ThemeDto theme1;
    static ThemeDto theme2;

    static SubThemeDto subTheme1;
    static SubThemeDto subTheme2;

    static CardDto card1;
    static CardDto card2;

    long theme1Id;
    long theme2Id;

    long subTheme1Id;
    long subTheme2Id;

    long card1Id;
    long card2Id;
    private static TestRestTemplate restTemplate = new TestRestTemplate();

    @Before
    public void init() {
        controller = new ThemeRestController(new ThemeRepoMock());
        theme1 = new ThemeDto(0, "School", "TestTheme Everything to do with school");
        theme2 = new ThemeDto(0, "Industry", "TestTheme Everything to do with Industry");

        subTheme1 = new SubThemeDto(0, "Extra classroom", "Build extra classrooms to expand student limit");
        subTheme2 = new SubThemeDto(0, "More recreation", "Allow students to relax more while on break");

        card1 = new CardDto(0, "Expanding rooms physically", "Make classrooms bigger by expanding surface", false);
        card2 = new CardDto(0, "Remove furniture", "Gain more surface by removing furniture", false);
        setupDb();
    }

    @Test
    public void TestCreateTheme() {
        ThemeDto themeDto = new ThemeDto(0, "JSONTheme", "Theme created via JSON");
        ResponseEntity<ThemeDto> response = restTemplate.postForEntity(callURL + "api/public/themes", themeDto, ThemeDto.class);
        ResponseEntity<ThemeDto> responseGet = restTemplate.getForEntity(callURL + "api/public/theme/" + response.getBody().getThemeId(), ThemeDto.class);
        Assert.assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        Assert.assertThat(responseGet.getStatusCode(), equalTo(HttpStatus.OK));
        Assert.assertThat(response.getBody().getClass(), equalTo(ThemeDto.class));
        Assert.assertThat(responseGet.getBody().getName(), equalTo("JSONTheme"));
    }

    @Test
    public void TestCreateSubTheme() {
        SubThemeDto subThemeDto = new SubThemeDto(0, "JSONSubTheme", "SubTheme created throughg JSON");
        ResponseEntity<SubThemeDto> response = restTemplate.postForEntity(callURL + "api/public/subthemes/" + theme1Id, subThemeDto, SubThemeDto.class);
        ResponseEntity<SubThemeDto> responseGet = restTemplate.getForEntity(callURL + "api/public/subtheme/" + response.getBody().getSubThemeId(), SubThemeDto.class);
        Assert.assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        Assert.assertThat(responseGet.getStatusCode(), equalTo(HttpStatus.OK));
        Assert.assertThat(responseGet.getBody().getSubThemeName(), equalTo(subThemeDto.getSubThemeName()));
        Assert.assertThat(responseGet.getBody().getSubThemeDescription(), equalTo(subThemeDto.getSubThemeDescription()));
        Assert.assertThat(responseGet.getBody().getTheme().getThemeId(), equalTo(theme1Id));
    }

    @Test
    public void TestGetAllThemes() {
        ParameterizedTypeReference<List<ThemeDto>> typeref = new ParameterizedTypeReference<List<ThemeDto>>() {
        };
        ResponseEntity<List<ThemeDto>> response = restTemplate.exchange(callURL + "api/public/themes", HttpMethod.GET, null, typeref);
        Assert.assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        for (ThemeDto t : response.getBody()
                ) {
            System.out.println(t.getName() + " - " + t.getDescription());
        }
        Assert.assertNotNull(response.getBody());
        Assert.assertThat(response.getBody().size(), equalTo(2));
    }

    @Test
    public void TestGetThemeById() {
        ResponseEntity<Theme> response1 = restTemplate.getForEntity(callURL + "api/public/theme/" + theme1Id, Theme.class);
        Assert.assertThat(response1.getStatusCode(), equalTo(HttpStatus.OK));
        Assert.assertThat(response1.getBody().getClass(), equalTo(Theme.class));
        Assert.assertThat(response1.getBody().getName(), equalTo(theme1.getName()));
        System.out.println(response1.getBody().getName());
        ResponseEntity<Theme> response2 = restTemplate.getForEntity(callURL + "api/public/theme/" + theme2Id, Theme.class);
        Assert.assertThat(response2.getStatusCode(), equalTo(HttpStatus.OK));
        Assert.assertThat(response2.getBody().getClass(), equalTo(Theme.class));
        Assert.assertThat(response2.getBody().getName(), equalTo(theme2.getName()));
        System.out.println(response2.getBody().getName());
    }

    @Test
    public void TestGetThemeByName() {
        ResponseEntity<ThemeDto> response = restTemplate.getForEntity(callURL + "api/public/theme?name=School", ThemeDto.class);
        Assert.assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        Assert.assertThat(response.getBody().getClass(), equalTo(ThemeDto.class));
        Assert.assertThat(response.getBody().getDescription(), equalTo(theme1.getDescription()));
    }

    @Test
    public void TestGetSubThemeById() {
        ResponseEntity<SubThemeDto> response = restTemplate.getForEntity(callURL + "api/public/subtheme/" + subTheme1Id, SubThemeDto.class);
        Assert.assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        Assert.assertThat(response.getBody().getSubThemeId(), equalTo(subTheme1Id));
        Assert.assertThat(response.getBody().getSubThemeName(), equalTo(subTheme1.getSubThemeName()));
    }

    @Test
    public void TestGetSubThemesByThemeId() {
        ParameterizedTypeReference<List<SubThemeDto>> typeref = new ParameterizedTypeReference<List<SubThemeDto>>() {
        };
        ResponseEntity<List<SubThemeDto>> response = restTemplate.exchange(callURL + "api/public/theme/" + theme1Id + "/subthemes", HttpMethod.GET, null, typeref);
        Assert.assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        Assert.assertThat(response.getBody().size(), equalTo(2));
    }

    @Test
    public void TestEditTheme() {
        ThemeDto themeDto = restTemplate.getForEntity(callURL + "api/public/theme/" + theme1Id, ThemeDto.class).getBody();
        themeDto.setName("SchoolUpdated");
        themeDto.setDescription("Updated Theme of School");
        HttpEntity<ThemeDto> httpEntity = new HttpEntity<>(themeDto);
        ResponseEntity<ThemeDto> response = restTemplate.exchange(callURL + "api/public/theme/" + theme1Id, HttpMethod.PUT, httpEntity, ThemeDto.class);

        Assert.assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        Assert.assertThat(response.getBody().getName(), not(theme1.getName()));
        Assert.assertThat(response.getBody().getDescription(), not(theme1.getDescription()));
        Assert.assertThat(response.getBody().getThemeId(), equalTo(themeDto.getThemeId()));
    }

    @Test
    public void TestEditSubTheme() {
        ThemeDto dtoForSubThemes = restTemplate.getForEntity(callURL + "api/public/theme/" + theme2Id, ThemeDto.class).getBody();
        SubThemeDto subThemeDto = restTemplate.getForEntity(callURL + "api/public/subtheme/" + subTheme1Id, SubThemeDto.class).getBody();
        subThemeDto.setSubThemeName("Updated SubTheme");
        subThemeDto.setSubThemeDescription("Updated Subtheme should not be equal to the old version");
        subThemeDto.setTheme(dtoForSubThemes);
        HttpEntity<SubThemeDto> httpEntity = new HttpEntity<>(subThemeDto);
        ResponseEntity<SubThemeDto> response = restTemplate.exchange(callURL + "api/public/subtheme/" + subTheme1Id, HttpMethod.PUT, httpEntity, SubThemeDto.class);

        Assert.assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        Assert.assertThat(response.getBody().getSubThemeName(), not(subTheme1.getSubThemeName()));
        Assert.assertThat(response.getBody().getSubThemeDescription(), not(subTheme1.getSubThemeDescription()));
        Assert.assertThat(response.getBody().getTheme().getThemeId(), equalTo(theme2Id));
    }

    @Test
    public void TestRemoveTheme() {
        ResponseEntity<ThemeDto> response = restTemplate.exchange(callURL + "api/public/theme/" + theme1Id, HttpMethod.DELETE, null, ThemeDto.class);
        ResponseEntity<ThemeDto> responseGet = restTemplate.getForEntity(callURL + "api/public/theme" + theme1Id, ThemeDto.class);
        Assert.assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        Assert.assertNotNull(response.getBody());
        Assert.assertThat(responseGet.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
        //Assert.assertThat(response.getBody().getClass(),equalTo(Theme.class));
        Assert.assertThat(response.getBody().getClass(), equalTo(ThemeDto.class));
        Assert.assertThat(response.getBody().getName(), equalTo("School"));
    }

    @Test
    public void TestRemoveNonExistingTheme() {
        ResponseEntity<Theme> response = restTemplate.exchange(callURL + "api/public/theme/" + theme2Id + 599, HttpMethod.DELETE, null, Theme.class);
        Assert.assertThat(response.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
    }

    @Test
    public void TestRemoveThemeById() {
        ResponseEntity<ThemeDto> responseDelete = restTemplate.exchange(callURL + "api/public/theme/" + theme2Id, HttpMethod.DELETE, null, ThemeDto.class);
        ResponseEntity<ThemeDto> responseGet = restTemplate.getForEntity(callURL + "api/public/theme/" + theme2Id, ThemeDto.class);
        Assert.assertThat(responseDelete.getStatusCode(), equalTo(HttpStatus.OK));
        Assert.assertThat("Found theme should be NULL", responseGet.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
    }

    @Test
    public void TestRemoveSubThemesByThemeId() {
        ParameterizedTypeReference<List<SubThemeDto>> typeref = new ParameterizedTypeReference<List<SubThemeDto>>() {
        };
        ResponseEntity<List<SubThemeDto>> responseDelete = restTemplate.exchange(callURL + "api/public/subthemes/" + theme1Id, HttpMethod.DELETE, null, typeref);
        Assert.assertThat(responseDelete.getStatusCode(), equalTo(HttpStatus.OK));
        Assert.assertThat(responseDelete.getBody().size(), equalTo(2));
    }

    @Test
    public void TestGetThemeNonExistent() {
        ResponseEntity<ThemeDto> responseDelete = restTemplate.exchange(callURL + "api/public/theme/" + theme2Id, HttpMethod.DELETE, null, ThemeDto.class);
        ResponseEntity<ThemeDto> responseGet = restTemplate.getForEntity(callURL + "api/public/theme/" + theme2Id, ThemeDto.class);
        Assert.assertThat(responseDelete.getStatusCode(), equalTo(HttpStatus.OK));
        Assert.assertThat(responseGet.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
    }

    @Test
    public void TestGetAllCards() {
        ParameterizedTypeReference<List<CardDto>> typeref = new ParameterizedTypeReference<List<CardDto>>() {
        };
        ResponseEntity<List<CardDto>> response = restTemplate.exchange(callURL + "api/public/cards", HttpMethod.GET, null, typeref);
        Assert.assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        Assert.assertThat(response.getBody().size(), equalTo(2));
    }

    @Test
    public void TestCreateCard() {
        CardDto dtoToAdd = new CardDto(0, "JSONCard", "Card should be added through testing", false);
        ResponseEntity<CardDto> response = restTemplate.postForEntity(callURL + "api/public/cards", dtoToAdd, CardDto.class);
        Assert.assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        Assert.assertThat(response.getBody().getName(), equalTo(dtoToAdd.getName()));
        Assert.assertThat(response.getBody().getDescription(), equalTo(dtoToAdd.getDescription()));
    }

    @Test
    public void TestAddCardToSubTheme() {
        CardDto mockCard = new CardDto(0, "MOCKCard", "Mock Card to test AddCardToTheme", false);
        mockCard = restTemplate.postForEntity(callURL + "api/public/cards", mockCard, CardDto.class).getBody();
        ResponseEntity<SubThemeDto> response = restTemplate.exchange(callURL + "api/public/subtheme/" + subTheme2Id + "/cards/" + mockCard.getCardId(), HttpMethod.POST, null, SubThemeDto.class);
        CardDto updatedCard = restTemplate.getForEntity(callURL + "api/public/card/" + mockCard.getCardId(), CardDto.class).getBody();
        SubThemeDto updatedSubTHeme = restTemplate.getForEntity(callURL + "api/public/subtheme/" + subTheme2Id, SubThemeDto.class).getBody();
        Assert.assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        Assert.assertThat(response.getBody().getSubThemeId(), equalTo(subTheme2Id));
        boolean cardInSubTheme = false;
        for (CardSubThemeDto c : response.getBody().getCardSubThemes()
                ) {
            if (c.getCard().getCardId() == mockCard.getCardId()) cardInSubTheme = true;
        }
        boolean subThemeInCard = false;
        for (CardSubThemeDto cst : updatedCard.getCardSubThemes()) {
            if (cst.getSubTheme().getSubThemeId() == subTheme2Id) subThemeInCard = true;
        }
        Assert.assertTrue("SubTheme should contain Card", cardInSubTheme);
        Assert.assertTrue("Card should contain SubTheme", subThemeInCard);
    }

    @Test
    public void TestGetCardById() {
        ResponseEntity<CardDto> response = restTemplate.getForEntity(callURL + "api/public/card/" + card1Id, CardDto.class);
        Assert.assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        Assert.assertThat(response.getBody().getCardId(), equalTo(card1Id));
    }

    @Test
    public void TestGetCardsBySubThemeId() {
        ParameterizedTypeReference<List<CardDto>> typeref = new ParameterizedTypeReference<List<CardDto>>() {
        };
        ResponseEntity<List<CardDto>> response = restTemplate.exchange(callURL + "api/public/subtheme/" + subTheme1Id + "/cards", HttpMethod.GET, null, typeref);
        Assert.assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        Assert.assertThat(response.getBody().size(), equalTo(2));
    }

    private void setupDb() {
        System.out.println(restTemplate.exchange(callURL + "api/public/themes", HttpMethod.DELETE, null, String.class).getStatusCode());
        ResponseEntity<ThemeDto> response1 = restTemplate.postForEntity(callURL + "api/public/themes", theme1, ThemeDto.class);
        theme1Id = response1.getBody().getThemeId();
        ResponseEntity<ThemeDto> response2 = restTemplate.postForEntity(callURL + "api/public/themes", theme2, ThemeDto.class);
        theme2Id = response2.getBody().getThemeId();
        ResponseEntity<SubThemeDto> response3 = restTemplate.postForEntity(callURL + "api/public/subthemes/" + theme1Id, subTheme1, SubThemeDto.class);
        subTheme1Id = response3.getBody().getSubThemeId();
        ResponseEntity<SubThemeDto> response4 = restTemplate.postForEntity(callURL + "api/public/subthemes/" + theme1Id, subTheme2, SubThemeDto.class);
        subTheme2Id = response4.getBody().getSubThemeId();

        ResponseEntity<CardDto> response5 = restTemplate.postForEntity(callURL + "api/public/cards", card1, CardDto.class);
        card1Id = response5.getBody().getCardId();
        ResponseEntity<CardDto> response6 = restTemplate.postForEntity(callURL + "api/public/cards", card2, CardDto.class);
        card2Id = response6.getBody().getCardId();

        ResponseEntity<SubThemeDto> response7 = restTemplate.exchange(callURL + "api/public/subtheme/" + subTheme1Id + "/cards/" + card1Id, HttpMethod.POST, null, SubThemeDto.class);
        ResponseEntity<SubThemeDto> response8 = restTemplate.exchange(callURL + "api/public/subtheme/" + subTheme1Id + "/cards/" + card2Id, HttpMethod.POST, null, SubThemeDto.class);

    }
}