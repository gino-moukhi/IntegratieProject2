package be.kdg.kandoe.integration;

import be.kdg.kandoe.domain.theme.SubTheme;
import be.kdg.kandoe.domain.theme.Theme;
import be.kdg.kandoe.dto.SubThemeDto;
import be.kdg.kandoe.dto.ThemeDto;
import be.kdg.kandoe.service.declaration.ThemeService;
import be.kdg.kandoe.service.exception.InputValidationException;
import be.kdg.kandoe.service.exception.ThemeServiceException;
import be.kdg.kandoe.service.implementation.ThemeServiceImpl;
import be.kdg.kandoe.unit.theme.ThemeRepoMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.validation.constraints.Null;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class TestThemeService {

    private ThemeService themeService;
    private Theme theme1;
    private Theme theme2;

    private SubTheme subTheme1;
    private SubTheme subTheme2;

    @Before
    public void Setup(){
        themeService = new ThemeServiceImpl(new ThemeRepoMock());
        ThemeDto theme1DTO=new ThemeDto(1L,"Jeugd","Acties voor de jeugd");
        ThemeDto theme2DTO= new ThemeDto(2L,"Sport","Acties voor sport");

        theme1 = new Theme(theme1DTO);
        theme2 = new Theme(theme2DTO);

        subTheme1= new SubTheme(new SubThemeDto(1L,null,"Speelplein","Speelplein aanleggen"));
        subTheme2 = new SubTheme(new SubThemeDto(2L,null,"Voetbalplein","Voetbalplein aanleggen"));

        themeService.addTheme(theme1);
        themeService.addTheme(theme2);

        themeService.addSubThemeByThemeId(subTheme1,theme1.getThemeId());
        themeService.addSubThemeByThemeId(subTheme2,theme1.getThemeId());
    }

    @Test
    public void TestAddTheme(){
        ThemeDto DTOToAdd = new ThemeDto(3L,"ThemeToAdd","Test Theme, if this exists success");
        Theme themeToAdd = new Theme(DTOToAdd);
        themeService.addTheme(themeToAdd);
        Theme returningTheme = themeService.getThemeByName(themeToAdd.getName());
        assertEquals("Theme returned should be equal to ThemeToAdd",returningTheme,themeToAdd);
    }

    /**
     * ThemeName can only contain 50 characters, if longer InputValidationException should be thrown
     */
    @Test(expected = InputValidationException.class)
    public void TestAddInvalidTheme(){
        ThemeDto DTOToAdd = new ThemeDto(3L,"ThemeToAdd123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789","Theme with a name that is too long");
        Theme ThemeToAdd = new Theme(DTOToAdd);
        themeService.addTheme(ThemeToAdd);
        Theme returningTheme = themeService.getThemeById(ThemeToAdd.getThemeId());
        assertEquals("Theme returned should be null, name too long", returningTheme,null);
    }

    @Test
    public void TestGetThemeById() {
        Theme theme = themeService.getThemeById(1);
        assertEquals("Returned theme should be equal", theme, theme1);
    }

    @Test
    public void TestGetAllThemes(){
        Theme theme = new Theme(new ThemeDto(3L,"Schoonheid","Thema ivm met schoonheid"));
        themeService.addTheme(theme);
        assertEquals("Length of themes should be 3",themeService.getAllThemes().size(),3);
        assertEquals("Values of themes should match",themeService.getThemeById(1).getName(),"Jeugd");
        assertEquals("Values of themes should match",themeService.getThemeById(2).getName(),"Sport");
        assertEquals("Values of themes should match",themeService.getThemeById(3).getName(),"Schoonheid");

    }

    @Test
    public void TestGetThemeByName(){
        Theme theme = themeService.getThemeByName(theme1.getName());
        assertEquals("Returned theme should be equal", theme,theme1);
    }

    @Test
    public void TestEditTheme(){
        ThemeDto updatedDTO = new ThemeDto(theme1.getThemeId(),theme1.getName(),theme1.getDescription());
        Theme newTheme = new Theme(updatedDTO);
        Theme oldTheme = new Theme(new ThemeDto(newTheme.getThemeId(),newTheme.getName(),newTheme.getDescription()));
        newTheme.setDescription("Theme has been updated");
        newTheme.setName("Updated Theme");
        Theme updatedTheme = themeService.editTheme(newTheme);
        Assert.assertNotSame("Themes should not be the same",oldTheme,updatedTheme);
    }
    /**
     * ThemeName can only contain 50 characters, if longer InputValidationException should be thrown
     */
    @Test(expected = InputValidationException.class)
    public void TestEditInvalidTheme(){
        Theme theme = theme1;
        theme.setName("Updated Theme 1234567891324684316846312316846132035461.065846464165468784513221341654631616316541321654131");
        theme.setDescription("Theme has been updated");
        themeService.editTheme(theme);
    }

    @Test
    public void TestDeleteTheme(){
        assertEquals(themeService.getAllThemes().size(),2);
        Theme themeToDelete = theme2;
        themeService.removeThemeById(themeToDelete.getThemeId());
        assertEquals(themeService.getAllThemes().size(),1);
    }
    @Test(expected = ThemeServiceException.class)
    public void TestDeleteThemeById(){
        Theme themeToDelete = theme1;
        themeService.removeThemeById(themeToDelete.getThemeId());
        Theme theme = themeService.getThemeById(themeToDelete.getThemeId());
        assertEquals("Returned theme should be NULL",theme,null);
    }

    /**
     * If the themeToDelete is not found or doesn't exist, ThemeServiceException should be thrown
     */
    @Test(expected = ThemeServiceException.class)
    public void TestDeleteByNonExistingThemeId(){
        Theme unknownTheme = new Theme(new ThemeDto(3L,"School","Thema ivm school"));
        themeService.removeThemeById(unknownTheme.getThemeId());
    }
    /**
     * If the themeToDelete is not found or doesn't exist, ThemeServiceException should be thrown
     */
    @Test(expected = ThemeServiceException.class)
    public void TestDeleteNonExistingTheme(){
        Theme unknownTheme = new Theme(new ThemeDto(3L,"Armoede", "Thema ivm armoeden enzo"));
        themeService.removeThemeById(unknownTheme.getThemeId());
    }

    @Test
    public void TestCreateSubTheme(){
        SubTheme subThemeToAdd = new SubTheme(new SubThemeDto(3L,null,"Activiteiten","Subthema voor Sport"));
        SubTheme subThemeReceived = themeService.addSubThemeByThemeId(subThemeToAdd,theme2.getThemeId());
        Assert.assertThat(subThemeReceived.getSubThemeId(),equalTo(subThemeToAdd.getSubThemeId()));
        Assert.assertThat(subThemeReceived.getTheme(),equalTo(subThemeToAdd.getTheme()));
        Assert.assertThat(subThemeReceived.getSubThemeDescription(),equalTo(subThemeToAdd.getSubThemeDescription()));
        Assert.assertThat(subThemeReceived.getSubThemeName(),equalTo(subThemeToAdd.getSubThemeName()));
    }

    @Test
    public void TestGetSubThemesByThemeId(){
        long themeId = 1;
        List<SubTheme> subthemes= themeService.getSubThemesByThemeId(themeId);
        Assert.assertThat(subthemes.size(),equalTo(2));
        Assert.assertThat(subthemes.get(0).getSubThemeName(),equalTo("Speelplein"));
    }

    @Test
    public void TestGetSubThemeById(){
        long subThemeId=2;
        SubTheme subTheme = themeService.getSubThemeById(subThemeId);
        assertNotNull(subTheme);
        assertThat(subTheme.getSubThemeName(),equalTo("Voetbalplein"));
        assertThat(subTheme.getSubThemeId(),equalTo(new Long(2)));
        assertThat(subTheme.getSubThemeDescription(),equalTo("Voetbalplein aanleggen"));
        assertNotNull(subTheme.getTheme());
    }

    @Test(expected = ThemeServiceException.class)
    public void TestDeleteSubThemeById(){
        long subThemeId=2;
        themeService.removeSubThemeById(subThemeId);
        themeService.getSubThemeById(2);
    }
}
