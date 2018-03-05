package be.kdg.kandoe.unit.theme;

import be.kdg.kandoe.domain.theme.SubTheme;
import be.kdg.kandoe.domain.theme.Theme;
import be.kdg.kandoe.dto.SubThemeDto;
import be.kdg.kandoe.dto.ThemeDto;
import be.kdg.kandoe.repository.jpa.SubThemeJpa;
import be.kdg.kandoe.repository.jpa.ThemeJpa;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.IsEqual.equalTo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class UnitTestThemeConversion {
    private Theme themeToTest;
    private ThemeDto themeDtoToTest;
    private ThemeJpa themeJpaToTest;

    private SubTheme subThemeToTest;
    private SubThemeDto subThemeDtoToTest;
    private SubThemeJpa subThemeJpaToTest;

    @Before
    public void Setup(){
        themeToTest= new Theme(new ThemeDto(1L,"School","Theme to test conversion"));
        themeDtoToTest=new ThemeDto(2L,"Industry","Testing if this theme stays the same");
        themeJpaToTest = new ThemeJpa(new Theme(new ThemeDto(3L,"Building","Try to make the conversion equal all the time")));

        subThemeToTest = new SubTheme(new SubThemeDto(1L,themeDtoToTest,"Product quality","Subtheme for theme: Industry"));
        subThemeDtoToTest = new SubThemeDto(2L,themeDtoToTest,"Employee hapiness", "2nd subtheme for theme: Industry");
        subThemeJpaToTest = new SubThemeJpa(new SubTheme(new SubThemeDto(3L,themeDtoToTest,"Business expansion","3rd subtheme for theme: Industry")));
    }

    @Test
    public void TestThemeToThemeDto(){
        ThemeDto dto = ThemeDto.fromTheme(themeToTest);
        Assert.assertThat(dto.getThemeId(),equalTo(themeToTest.getThemeId()));
        Assert.assertThat(dto.getName(),equalTo(themeToTest.getName()));
        Assert.assertThat(dto.getDescription(),equalTo(themeToTest.getDescription()));
    }

    @Test
    public void TestThemeDtoToTheme(){
        Theme theme = themeDtoToTest.toTheme();
        Assert.assertThat(theme.getThemeId(),equalTo(themeDtoToTest.getThemeId()));
        Assert.assertThat(theme.getName(),equalTo(themeDtoToTest.getName()));
        Assert.assertThat(theme.getDescription(),equalTo(themeDtoToTest.getDescription()));
    }

    @Test
    public void TestThemeJpaToTheme(){
        Theme theme = themeJpaToTest.toTheme();
        Assert.assertThat(theme.getThemeId(),equalTo(themeJpaToTest.getThemeId()));
        Assert.assertThat(theme.getName(),equalTo(themeJpaToTest.getName()));
        Assert.assertThat(theme.getDescription(),equalTo(themeJpaToTest.getDescription()));
    }

    @Test
    public void TestThemeToThemeJpa(){
        ThemeJpa jpa = ThemeJpa.fromTheme(themeToTest);
        Assert.assertThat(themeToTest.getThemeId(),equalTo(jpa.getThemeId()));
        Assert.assertThat(themeToTest.getName(),equalTo(jpa.getName()));
        Assert.assertThat(themeToTest.getDescription(),equalTo(jpa.getDescription()));
    }

    @Test
    public void TestThemeDtoToJSON(){
        String JSON = themeDtoToTest.toJsonString();
        Assert.assertTrue(isJSONValid(JSON));
    }

     @Test
     public void TestSubThemeDtoToJSON(){
        String JSON = subThemeDtoToTest.toJsonString();
        Assert.assertTrue(isJSONValid(JSON));
     }

    @Test
    public void TestSubThemeToSubThemeDto(){
        SubThemeDto dto = SubThemeDto.fromSubTheme(subThemeToTest);
        Assert.assertThat(dto.getTheme().getThemeId(),equalTo(subThemeToTest.getTheme().getThemeId()));
        Assert.assertThat(dto.getTheme().getName(),equalTo(subThemeToTest.getTheme().getName()));
        Assert.assertThat(dto.getTheme().getDescription(),equalTo(subThemeToTest.getTheme().getDescription()));
        Assert.assertThat(dto.getTheme().getClass(),not(subThemeToTest.getTheme().getClass()));
        Assert.assertThat(dto.getSubThemeId(),equalTo(subThemeToTest.getSubThemeId()));
        Assert.assertThat(dto.getSubThemeName(),equalTo(subThemeToTest.getSubThemeName()));
        Assert.assertThat(dto.getSubThemeDescription(),equalTo(subThemeToTest.getSubThemeDescription()));
    }

    @Test
    public void TestSubThemeToSubThemeJpa(){
        SubThemeJpa jpa = SubThemeJpa.fromSubTheme(subThemeToTest);
        Assert.assertThat(jpa.getTheme().getThemeId(),equalTo(subThemeToTest.getTheme().getThemeId()));
        Assert.assertThat(jpa.getTheme().getName(),equalTo(subThemeToTest.getTheme().getName()));
        Assert.assertThat(jpa.getTheme().getDescription(),equalTo(subThemeToTest.getTheme().getDescription()));
        Assert.assertThat(jpa.getTheme().getClass(),not(subThemeToTest.getTheme().getClass()));
        Assert.assertThat(jpa.getSubThemeId(),equalTo(subThemeToTest.getSubThemeId()));
        Assert.assertThat(jpa.getSubThemeName(),equalTo(subThemeToTest.getSubThemeName()));
        Assert.assertThat(jpa.getSubThemeDescription(),equalTo(subThemeToTest.getSubThemeDescription()));
    }

    @Test
    public void TestSubThemeDtoToSubTheme(){
        SubTheme subTheme = subThemeDtoToTest.toSubTheme();
        Assert.assertThat(subTheme.getTheme().getClass(),not(subThemeDtoToTest.getTheme()));
        Assert.assertThat(subTheme.getTheme().getThemeId(),equalTo(subThemeDtoToTest.getTheme().getThemeId()));
        Assert.assertThat(subTheme.getTheme().getName(),equalTo(subThemeDtoToTest.getTheme().getName()));
        Assert.assertThat(subTheme.getTheme().getDescription(),equalTo(subThemeDtoToTest.getTheme().getDescription()));
        Assert.assertThat(subTheme.getSubThemeId(),equalTo(subThemeDtoToTest.getSubThemeId()));
        Assert.assertThat(subTheme.getSubThemeName(),equalTo(subThemeDtoToTest.getSubThemeName()));
        Assert.assertThat(subTheme.getSubThemeDescription(),equalTo(subThemeDtoToTest.getSubThemeDescription()));
    }

    @Test
    public void TestSubThemeJpaToSubTheme(){
        SubTheme subThemeFromJpa = subThemeJpaToTest.toSubTheme();
        Assert.assertThat(subThemeFromJpa.getTheme().getClass(),not(subThemeJpaToTest.getTheme()));
        Assert.assertThat(subThemeFromJpa.getTheme().getThemeId(),equalTo(subThemeJpaToTest.getTheme().getThemeId()));
        Assert.assertThat(subThemeFromJpa.getTheme().getName(),equalTo(subThemeJpaToTest.getTheme().getName()));
        Assert.assertThat(subThemeFromJpa.getTheme().getDescription(),equalTo(subThemeJpaToTest.getTheme().getDescription()));
        Assert.assertThat(subThemeFromJpa.getSubThemeId(),equalTo(subThemeJpaToTest.getSubThemeId()));
        Assert.assertThat(subThemeFromJpa.getSubThemeName(),equalTo(subThemeJpaToTest.getSubThemeName()));
        Assert.assertThat(subThemeFromJpa.getSubThemeDescription(),equalTo(subThemeJpaToTest.getSubThemeDescription()));
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
