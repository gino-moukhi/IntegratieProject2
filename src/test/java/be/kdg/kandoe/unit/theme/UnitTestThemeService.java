package be.kdg.kandoe.unit.theme;

import be.kdg.kandoe.domain.theme.Theme;
import be.kdg.kandoe.dto.ThemeDTO;
import be.kdg.kandoe.service.declaration.ThemeService;
import be.kdg.kandoe.service.exception.InputValidationException;
import be.kdg.kandoe.service.implementation.ThemeServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class UnitTestThemeService {

    private ThemeService themeService;
    private Theme theme1;
    private Theme theme2;

    @Before
    public void Setup(){
        themeService = new ThemeServiceImpl(new ThemeRepoMock());
        ThemeDTO theme1DTO=new ThemeDTO(0,"Jeugd","Acties voor de jeugd");
        ThemeDTO theme2DTO= new ThemeDTO(1,"Sport","Acties voor sport");
        theme1 = new Theme(theme1DTO);
        theme2 = new Theme(theme2DTO);
        themeService.addTheme(theme1);
        themeService.addTheme(theme2);
    }

    @Test
    public void TestAddTheme(){
        ThemeDTO DTOToAdd = new ThemeDTO(2,"ThemeToAdd","Test Theme, if this exists success");
        Theme themeToAdd = new Theme(DTOToAdd);
        themeService.addTheme(themeToAdd);
        Theme returningTheme = themeService.getThemeByName(themeToAdd.getName());
        assertEquals("Theme returned should be equal to ThemeToAdd",returningTheme,themeToAdd);
    }

    @Test(expected = InputValidationException.class)
    public void TestAddInvalidTheme(){
        ThemeDTO DTOToAdd = new ThemeDTO(2,"ThemeToAdd123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789","Theme with a name that is too long");
        Theme ThemeToAdd = new Theme(DTOToAdd);
        themeService.addTheme(ThemeToAdd);
        Theme returningTheme = themeService.getThemeById(ThemeToAdd.getThemeId());
        assertEquals("Theme returned should be null, name too long", returningTheme,null);
    }

    @Test
    public void TestGetThemeById() {
        Theme theme = themeService.getThemeById(0);
        assertEquals("Returned theme should be equal", theme, theme1);
    }
    @Test
    public void TestGetThemeByName(){
        Theme theme = themeService.getThemeByName(theme1.getName());
        assertEquals("Returned theme should be equal", theme,theme1);
    }
//TODO
/*    @Test
    public void TestEditTheme(){
        Theme theme = theme1;
        theme.setDescription("Theme has been updated");
        theme.setName("Updated Theme");
        Theme updatedTheme = themeService.editTheme(theme);
        Assert.assertNotSame("Themes should not be the same",theme,updatedTheme);
    }*/

    @Test(expected = InputValidationException.class)
    public void TestEditInvalidTheme(){
        Theme theme = theme1;
        theme.setName("Updated Theme 1234567891324684316846312316846132035461.065846464165468784513221341654631616316541321654131");
        theme.setDescription("Theme has been updated");
        themeService.editTheme(theme);
    }
}
