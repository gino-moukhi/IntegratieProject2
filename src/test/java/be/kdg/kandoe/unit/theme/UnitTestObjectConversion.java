package be.kdg.kandoe.unit.theme;

import be.kdg.kandoe.domain.theme.Card;
import be.kdg.kandoe.domain.theme.CardSubTheme;
import be.kdg.kandoe.domain.theme.SubTheme;
import be.kdg.kandoe.domain.theme.Theme;
import be.kdg.kandoe.dto.converter.DtoConverter;
import be.kdg.kandoe.dto.theme.CardDto;
import be.kdg.kandoe.dto.theme.CardSubThemeDto;
import be.kdg.kandoe.dto.theme.SubThemeDto;
import be.kdg.kandoe.dto.theme.ThemeDto;
import be.kdg.kandoe.repository.jpa.CardJpa;
import be.kdg.kandoe.repository.jpa.CardSubThemeJpa;
import be.kdg.kandoe.repository.jpa.SubThemeJpa;
import be.kdg.kandoe.repository.jpa.ThemeJpa;
import be.kdg.kandoe.repository.jpa.converter.JpaConverter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.IsEqual.equalTo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class UnitTestObjectConversion {
    private Theme themeToTest;
    private ThemeDto themeDtoToTest;
    private ThemeJpa themeJpaToTest;

    private SubTheme subThemeToTest;
    private SubTheme subThemeToTestCards;
    private SubThemeJpa subThemeJpaToTestCards;
    private SubThemeDto subThemeDtoToTestCards;
    private SubThemeDto subThemeDtoToTest;
    private SubThemeJpa subThemeJpaToTest;

    private Card cardToTest;
    private CardDto cardDtoToTest;
    private CardJpa cardJpaToTest;

    @Before
    public void Setup() {
        themeToTest = new Theme();
        themeToTest.setThemeId(new Long(1));
        themeToTest.setName("School");
        themeToTest.setDescription("Theme to test conversion");

        themeDtoToTest = new ThemeDto(2, "Industry", "Testing if this theme stays the same");
        themeJpaToTest = new ThemeJpa();
        themeJpaToTest.setThemeId(3);
        themeJpaToTest.setName("Building");
        themeJpaToTest.setDescription("Try to make the conversion equal all the time");

        subThemeToTest = new SubTheme();
        subThemeToTest.setSubThemeId(new Long(1));
        subThemeToTest.setSubThemeName("Product Quality");
        subThemeToTest.setSubThemeDescription("Subtheme for theme: Industry");
        subThemeToTest.setTheme(themeToTest);

        subThemeDtoToTest = new SubThemeDto(2, themeDtoToTest, "Employee hapiness", "2nd subtheme for theme: Industry", new ArrayList<>());

        subThemeJpaToTest = new SubThemeJpa();
        subThemeJpaToTest.setSubThemeId(new Long(3));
        subThemeJpaToTest.setSubThemeName("Business Expansion");
        subThemeJpaToTest.setSubThemeDescription("3rd subtheme for theme: Industry");
        subThemeJpaToTest.setTheme(themeJpaToTest);

        subThemeToTestCards = new SubTheme();
        subThemeToTestCards.setSubThemeId(new Long(4));
        subThemeToTestCards.setSubThemeName("Development");
        subThemeToTestCards.setSubThemeDescription("Improving development");
        subThemeToTestCards.setTheme(themeToTest);

        subThemeJpaToTestCards = new SubThemeJpa();
        subThemeJpaToTestCards.setSubThemeId(new Long(5));
        subThemeJpaToTestCards.setSubThemeName("Development");
        subThemeJpaToTestCards.setSubThemeDescription("Improving development");
        subThemeJpaToTestCards.setTheme(themeJpaToTest);

        subThemeDtoToTestCards = new SubThemeDto(new Long(6), "Development", "Improving development");
        CardSubThemeDto cstDto = new CardSubThemeDto(3, cardDtoToTest, subThemeDtoToTest);
        cardDtoToTest.setCardSubThemes(Arrays.asList(new CardSubThemeDto[]{cstDto}));
        subThemeDtoToTest.setCardSubThemes(Arrays.asList(new CardSubThemeDto[]{cstDto}));

        cardToTest = new Card();
        cardToTest.setCardId(1);
        cardToTest.setName("Better packaging");
        cardToTest.setDescription("Longer lasting product");
        cardToTest.setDefaultCard(false);
        CardSubTheme cst = new CardSubTheme(1, cardToTest, subThemeToTest);
        cardToTest.setCardSubThemes(Arrays.asList(new CardSubTheme[]{cst}));
        subThemeToTest.setCardSubThemes(Arrays.asList(new CardSubTheme[]{cst}));

        cardDtoToTest = new CardDto(2, "Bigger platform", "Expanding platform size", false);
        cardJpaToTest = new CardJpa();
        cardJpaToTest.setCardId(3);
        cardJpaToTest.setName("Losing weight");
        cardJpaToTest.setDescription("Just fat");
        cardJpaToTest.setDefaultCard(false);
        CardSubThemeJpa cstJpa = new CardSubThemeJpa(2, cardJpaToTest, subThemeJpaToTest);
        cardJpaToTest.setCardSubThemes(Arrays.asList(new CardSubThemeJpa[]{cstJpa}));
        subThemeJpaToTest.setCardSubThemes(Arrays.asList(new CardSubThemeJpa[]{cstJpa}));

    }

    @Test
    public void TestThemeToThemeDto() {
        ThemeDto dto = DtoConverter.toThemeDto(themeToTest, false);
        Assert.assertThat(dto.getThemeId(), equalTo(themeToTest.getThemeId()));
        Assert.assertThat(dto.getName(), equalTo(themeToTest.getName()));
        Assert.assertThat(dto.getDescription(), equalTo(themeToTest.getDescription()));
    }

    @Test
    public void TestThemeDtoToTheme() {
        Theme theme = DtoConverter.toTheme(themeDtoToTest, false);
        Assert.assertThat(theme.getThemeId(), equalTo(themeDtoToTest.getThemeId()));
        Assert.assertThat(theme.getName(), equalTo(themeDtoToTest.getName()));
        Assert.assertThat(theme.getDescription(), equalTo(themeDtoToTest.getDescription()));
    }

    @Test
    public void TestThemeJpaToTheme() {
        Theme theme = JpaConverter.toTheme(themeJpaToTest, false);
        Assert.assertThat(theme.getThemeId(), equalTo(themeJpaToTest.getThemeId()));
        Assert.assertThat(theme.getName(), equalTo(themeJpaToTest.getName()));
        Assert.assertThat(theme.getDescription(), equalTo(themeJpaToTest.getDescription()));
    }

    @Test
    public void TestThemeToThemeJpa() {
        ThemeJpa jpa = JpaConverter.toThemeJpa(themeToTest, false);
        Assert.assertThat(themeToTest.getThemeId(), equalTo(jpa.getThemeId()));
        Assert.assertThat(themeToTest.getName(), equalTo(jpa.getName()));
        Assert.assertThat(themeToTest.getDescription(), equalTo(jpa.getDescription()));
    }

    @Test
    public void TestSubThemeToSubThemeDto() {
        SubThemeDto dto = DtoConverter.toSubThemeDto(subThemeToTest, false);
        Assert.assertThat(dto.getTheme().getThemeId(), equalTo(subThemeToTest.getTheme().getThemeId()));
        Assert.assertThat(dto.getTheme().getName(), equalTo(subThemeToTest.getTheme().getName()));
        Assert.assertThat(dto.getTheme().getDescription(), equalTo(subThemeToTest.getTheme().getDescription()));
        Assert.assertThat(dto.getSubThemeId(), equalTo(subThemeToTest.getSubThemeId()));
        Assert.assertThat(dto.getSubThemeName(), equalTo(subThemeToTest.getSubThemeName()));
        Assert.assertThat(dto.getSubThemeDescription(), equalTo(subThemeToTest.getSubThemeDescription()));
        Assert.assertThat(dto.getCardSubThemes().size(), equalTo(subThemeToTest.getCardSubThemes().size()));
        Assert.assertThat(dto.getCardSubThemes().get(0).getSubTheme(), equalTo(cardToTest.getCardId()));
    }

    @Test
    public void TestSubThemeToSubThemeJpa() {
        SubThemeJpa jpa = JpaConverter.toSubThemeJpa(subThemeToTest, false);
        Assert.assertThat(jpa.getTheme().getThemeId(), equalTo(subThemeToTest.getTheme().getThemeId()));
        Assert.assertThat(jpa.getTheme().getName(), equalTo(subThemeToTest.getTheme().getName()));
        Assert.assertThat(jpa.getTheme().getDescription(), equalTo(subThemeToTest.getTheme().getDescription()));
        Assert.assertThat(jpa.getTheme().getClass(), not(subThemeToTest.getTheme().getClass()));
        Assert.assertThat(jpa.getSubThemeId(), equalTo(subThemeToTest.getSubThemeId()));
        Assert.assertThat(jpa.getSubThemeName(), equalTo(subThemeToTest.getSubThemeName()));
        Assert.assertThat(jpa.getSubThemeDescription(), equalTo(subThemeToTest.getSubThemeDescription()));
        Assert.assertThat(jpa.getCardSubThemes().size(), equalTo(subThemeToTest.getCardSubThemes().size()));
    }

    @Test
    public void TestSubThemeDtoToSubTheme() {
        SubTheme subTheme = DtoConverter.toSubTheme(subThemeDtoToTest, false);
        Assert.assertThat(subTheme.getTheme().getClass(), not(subThemeDtoToTest.getTheme()));
        Assert.assertThat(subTheme.getTheme().getThemeId(), equalTo(subThemeDtoToTest.getTheme().getThemeId()));
        Assert.assertThat(subTheme.getTheme().getName(), equalTo(subThemeDtoToTest.getTheme().getName()));
        Assert.assertThat(subTheme.getTheme().getDescription(), equalTo(subThemeDtoToTest.getTheme().getDescription()));
        Assert.assertThat(subTheme.getSubThemeId(), equalTo(subThemeDtoToTest.getSubThemeId()));
        Assert.assertThat(subTheme.getSubThemeName(), equalTo(subThemeDtoToTest.getSubThemeName()));
        Assert.assertThat(subTheme.getSubThemeDescription(), equalTo(subThemeDtoToTest.getSubThemeDescription()));
        Assert.assertThat(subTheme.getCardSubThemes().size(), equalTo(subThemeDtoToTest.getCardSubThemes().size()));
    }

    @Test
    public void TestSubThemeJpaToSubTheme() {
        SubTheme subThemeFromJpa = JpaConverter.toSubTheme(subThemeJpaToTest, false);
        Assert.assertThat(subThemeFromJpa.getTheme().getClass(), not(subThemeJpaToTest.getTheme()));
        Assert.assertThat(subThemeFromJpa.getTheme().getThemeId(), equalTo(subThemeJpaToTest.getTheme().getThemeId()));
        Assert.assertThat(subThemeFromJpa.getTheme().getName(), equalTo(subThemeJpaToTest.getTheme().getName()));
        Assert.assertThat(subThemeFromJpa.getTheme().getDescription(), equalTo(subThemeJpaToTest.getTheme().getDescription()));
        Assert.assertThat(subThemeFromJpa.getSubThemeId(), equalTo(subThemeJpaToTest.getSubThemeId()));
        Assert.assertThat(subThemeFromJpa.getSubThemeName(), equalTo(subThemeJpaToTest.getSubThemeName()));
        Assert.assertThat(subThemeFromJpa.getSubThemeDescription(), equalTo(subThemeJpaToTest.getSubThemeDescription()));
        Assert.assertThat(subThemeFromJpa.getCardSubThemes().size(), equalTo(1));
    }

    @Test
    public void TestCardJpaToCard() {
        Card card = JpaConverter.toCard(cardJpaToTest, false);
    }

    public boolean isJSONValid(String test) {
        try {
            new JSONObject(test);
        } catch (JSONException ex) {
            // edited, to include @Arthur's comment
            // e.g. in case JSONArray is valid as well...
            try {
                new JSONArray(test);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }

}
