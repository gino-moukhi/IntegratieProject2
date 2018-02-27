package be.kdg.kandoe.unit.theme;

import be.kdg.kandoe.domain.theme.Theme;
import be.kdg.kandoe.dto.ThemeDto;
import be.kdg.kandoe.repository.jpa.ThemeJpa;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.core.IsEqual.equalTo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class UnitTestThemeConversion {
    private Theme themeToTest;
    private ThemeDto themeDtoToTest;
    private ThemeJpa themeJpaToTest;


    @Before
    public void Setup(){
        themeToTest= new Theme(new ThemeDto(1,"School","Theme to test conversion"));
        themeDtoToTest=new ThemeDto(2,"Industry","Testing if this theme stays the same");
        themeJpaToTest = new ThemeJpa(new Theme(new ThemeDto(3,"Building","Try to make the conversion equal all the time")));
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

}
