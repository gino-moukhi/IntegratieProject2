package be.kdg.kandoe.integration;

import be.kdg.kandoe.domain.theme.Card;
import be.kdg.kandoe.domain.theme.CardSubTheme;
import be.kdg.kandoe.domain.theme.SubTheme;
import be.kdg.kandoe.domain.theme.Theme;
import be.kdg.kandoe.dto.converter.DtoConverter;
import be.kdg.kandoe.dto.theme.CardDto;
import be.kdg.kandoe.dto.theme.SubThemeDto;
import be.kdg.kandoe.dto.theme.ThemeDto;
import be.kdg.kandoe.repository.declaration.ThemeRepository;
import be.kdg.kandoe.service.declaration.ThemeService;
import be.kdg.kandoe.service.exception.InputValidationException;
import be.kdg.kandoe.service.exception.ThemeRepositoryException;
import be.kdg.kandoe.service.exception.ThemeServiceException;
import be.kdg.kandoe.service.implementation.ThemeServiceImpl;
import be.kdg.kandoe.unit.theme.ThemeRepoMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class TestThemeService {

    private ThemeService themeService;

    private ThemeRepoMock repo;
    private Theme theme1;
    private Theme theme2;

    private SubTheme subTheme1;
    private SubTheme subTheme2;

    private Card card1;
    private Card card2;

    @Before
    public void Setup(){
        repo = new ThemeRepoMock();

        ThemeDto theme1DTO=new ThemeDto(1,"Jeugd","Acties voor de jeugd");
        ThemeDto theme2DTO= new ThemeDto(2,"Sport","Acties voor sport");

        theme1 = DtoConverter.toTheme(theme1DTO,false);
        theme2 = DtoConverter.toTheme(theme2DTO,false);

        subTheme1= new SubTheme();
        subTheme1.setSubThemeId(new Long(1));
        subTheme1.setSubThemeName("Speelplein");
        subTheme1.setSubThemeDescription("Speelplein aanleggen");
        subTheme1.setTheme(theme1);
        subTheme2 = new SubTheme();
        subTheme2.setSubThemeId(new Long(2));
        subTheme2.setSubThemeName("Voetbalplein");
        subTheme2.setSubThemeDescription("Voetbalplein aanleggen");
        subTheme2.setTheme(theme1);

        card1 = new Card();
        card1.setCardId(1);
        card1.setName("Schommel");
        card1.setDescription("Schommel aanleggen");
        card1.setDefaultCard(false);
        CardSubTheme cst1 = new CardSubTheme(1,card1,subTheme1);
        card1.setCardSubThemes(Arrays.asList(new CardSubTheme[]{cst1}));

        card2 = new Card();
        card2.setCardId(2);
        card2.setName("Surveillance");
        card2.setDescription("Beveiliging en camerabewaking");
        card2.setDefaultCard(false);
        CardSubTheme cst2 = new CardSubTheme(2,card2,subTheme1);
        CardSubTheme cst3 = new CardSubTheme(3, card2,subTheme2);
        card2.setCardSubThemes(Arrays.asList(new CardSubTheme[]{cst2}));

        subTheme1.setCardSubThemes(Arrays.asList(new CardSubTheme[]{cst1,cst2}));
        subTheme2.setCardSubThemes(Arrays.asList(new CardSubTheme[]{cst3}));

        repo.setThemes(new ArrayList(Arrays.asList(new Theme[]{theme1,theme2})));
        repo.setSubThemes(new ArrayList(Arrays.asList(new SubTheme[]{subTheme1,subTheme2})));
        repo.setCards(new ArrayList(Arrays.asList(new Card[]{card1,card2})));

        themeService = new ThemeServiceImpl(repo);

    }

    @Test
    public void TestAddTheme(){
        Theme themeToAdd = DtoConverter.toTheme(new ThemeDto(3,"ThemeToAdd","Test Theme, if this exists success"),false);
        themeService.addTheme(themeToAdd);
        Theme returningTheme = themeService.getThemeByName(themeToAdd.getName());
        assertEquals("Theme returned should be equal to ThemeToAdd",returningTheme,themeToAdd);
    }

    /**
     * ThemeName can only contain 50 characters, if longer InputValidationException should be thrown
     */
    @Test(expected = InputValidationException.class)
    public void TestAddInvalidTheme(){
        ThemeDto DTOToAdd = new ThemeDto(3,"ThemeToAdd123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789","Theme with a name that is too long");
        Theme ThemeToAdd = DtoConverter.toTheme(DTOToAdd,false);
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
        Theme theme = DtoConverter.toTheme(new ThemeDto(3,"Schoonheid","Thema ivm met schoonheid"),false);
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
        Theme newTheme = DtoConverter.toTheme(updatedDTO,false);
        Theme oldTheme =DtoConverter.toTheme(new ThemeDto(newTheme.getThemeId(),newTheme.getName(),newTheme.getDescription()),false);
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
        themeService.removeThemeById(theme2.getThemeId());
        assertEquals(themeService.getAllThemes().size(),1);
    }
    @Test(expected = ThemeRepositoryException.class)
    public void TestDeleteThemeById(){
        Theme themeToDelete = theme1;
        themeService.removeThemeById(themeToDelete.getThemeId());
        Theme theme = themeService.getThemeById(themeToDelete.getThemeId());
        assertEquals("Returned theme should be NULL",theme,null);
    }

    /**
     * If the themeToDelete is not found or doesn't exist, ThemeServiceException should be thrown
     */
    @Test(expected = ThemeRepositoryException.class)
    public void TestDeleteByNonExistingThemeId(){
        Theme unknownTheme = DtoConverter.toTheme(new ThemeDto(3,"School","Thema ivm school"),false);
        themeService.removeThemeById(unknownTheme.getThemeId());
    }
    /**
     * If the themeToDelete is not found or doesn't exist, ThemeServiceException should be thrown
     */
    @Test(expected = ThemeRepositoryException.class)
    public void TestDeleteNonExistingTheme(){
        themeService.removeThemeById(25);
    }

    @Test
    public void TestCreateSubTheme(){
        SubTheme subThemeToAdd = DtoConverter.toSubTheme(new SubThemeDto(3,null,"Activiteiten","Subthema voor Sport",new ArrayList<>()),false);
        subThemeToAdd.setSubThemeId(3);
        subThemeToAdd.setSubThemeName("Activiteiten");
        subThemeToAdd.setSubThemeDescription("Subthema voor sport");
        subThemeToAdd.setCardSubThemes(new ArrayList<>());

        SubTheme subThemeReceived = themeService.addSubThemeByThemeId(subThemeToAdd,theme2.getThemeId());
        Assert.assertThat(subThemeReceived.getSubThemeId(),equalTo(subThemeToAdd.getSubThemeId()));
        Assert.assertThat(subThemeReceived.getTheme().getThemeId(),equalTo(theme2.getThemeId()));
        Assert.assertThat(subThemeReceived.getTheme().getName(),equalTo(theme2.getName()));
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

    @Test(expected = ThemeRepositoryException.class)
    public void TestDeleteSubThemeById(){
        long subThemeId=2;
        themeService.removeSubThemeById(subThemeId);
        themeService.getSubThemeById(2);
    }

    @Test
    public void TestGetCardById(){
        long cardId=1;
        Card card =themeService.getCardById(cardId);
        Assert.assertThat(card.getCardId(),equalTo(cardId));
        Assert.assertThat(card.getName(),equalTo(card1.getName()));
        boolean contains = false;
        for (CardSubTheme cst: card.getCardSubThemes()){
            if(cst.getSubTheme().getSubThemeId()==subTheme1.getSubThemeId()){
                contains=true;
            }
        }
        Assert.assertTrue(contains);
    }

    @Test
    public void TestCreateCard(){
        Card newCard = DtoConverter.toCard(new CardDto(3,"CardToCreate","Card should be created by TestCreateCard",false),false);
        Card addedCard = themeService.addCard(newCard);
        Assert.assertThat(addedCard.getCardId(),equalTo(newCard.getCardId()));
        Assert.assertThat(addedCard.getName(),equalTo(newCard.getName()));
        Assert.assertThat(addedCard.getDescription(),equalTo(newCard.getDescription()));
    }

    @Test
    public void TestAddCardToSubtheme(){
        Card mockCard = DtoConverter.toCard(new CardDto(3,"Card of Subtheme","This Card should be added to a subtheme",false),false);
        SubTheme mockSubTheme = DtoConverter.toSubTheme(new SubThemeDto(3,null,"SubTheme of Card","This subtheme should contain a card",new ArrayList<>()),false);
        themeService.addSubThemeByThemeId(mockSubTheme,1);
        themeService.addCard(mockCard);
        SubTheme editedSubTheme = themeService.addCardToSubTheme(3,3);
        Card addedCard = themeService.getCardById(3);

        boolean subThemeContainsCard=false;
        SubTheme subThemeToGet = themeService.getSubThemeById(3);
        for (CardSubTheme cst:subThemeToGet.getCardSubThemes()
             ) {
            if(cst.getCard().getCardId()==mockCard.getCardId()){
                subThemeContainsCard=true;
            }
        }

        boolean cardContainsSubTheme=false;
        for (CardSubTheme cst: addedCard.getCardSubThemes()){
            if(cst.getSubTheme().getSubThemeId()==editedSubTheme.getSubThemeId()){
                cardContainsSubTheme=true;
            }
        }
        Assert.assertTrue("Subtheme should contain the card",subThemeContainsCard);
        Assert.assertTrue("Card should contain the subtheme",cardContainsSubTheme);
    }

    @Test
    public void DeleteCardsFromSubTheme(){
        Assert.assertThat("SubTheme should initially have cards",subTheme1.getCardSubThemes().size(),not(0));
        SubTheme subThemeNoCards = themeService.removeCardsFromSubTheme(1);
        Assert.assertThat(subThemeNoCards.getCardSubThemes().size(),equalTo(0));
    }

    @Test
    public void TestGetCardsFromSubTheme(){
        List<Card> cards = themeService.getCardsBySubthemeId(1);
        Assert.assertThat(cards.size(),equalTo(2));
    }

    @Test(expected = ThemeRepositoryException.class)
    public void TestGetNonExistingCard(){
        Card card = themeService.getCardById(5);
    }
}
