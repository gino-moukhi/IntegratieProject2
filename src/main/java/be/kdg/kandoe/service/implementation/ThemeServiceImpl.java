package be.kdg.kandoe.service.implementation;

import be.kdg.kandoe.domain.theme.Card;
import be.kdg.kandoe.domain.theme.SubTheme;
import be.kdg.kandoe.domain.theme.Theme;
import be.kdg.kandoe.repository.declaration.ThemeRepository;
import be.kdg.kandoe.service.declaration.ThemeService;
import be.kdg.kandoe.service.exception.InputValidationException;
import be.kdg.kandoe.service.exception.ThemeServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ThemeServiceImpl implements ThemeService {

    private ThemeRepository themeRepo;

    @Autowired
    public ThemeServiceImpl(ThemeRepository repository){
        this.themeRepo=repository;
    }
    //ADD-METHODS
    @Override
    public Theme addTheme(Theme theme) {
        if(checkNameLength(theme)) {
           return themeRepo.createTheme(theme);
        } else {
            throw new InputValidationException("Theme name too long");
        }
    }

    @Override
    public SubTheme addSubThemeByThemeId(SubTheme subTheme,long themeId) {
        Theme themeToAdd=themeRepo.findThemeById(themeId);
        subTheme.setTheme(themeToAdd);
        return themeRepo.createSubTheme(subTheme);
    }
    //ADD-METHODS
    //GET-METHODS
    @Override
    public Theme getThemeByName(String name) {
        Theme themeToFind = themeRepo.findThemeByName(name);
        return themeToFind;
    }

    @Override
    public Theme getThemeById(long id) {
        Theme foundTheme = themeRepo.findThemeById(id);
        return themeRepo.findThemeById(id);
    }

    @Override
    public SubTheme getSubThemeById(long subThemeId){
        SubTheme foundSubTheme = themeRepo.findSubThemeById(subThemeId);
        return foundSubTheme;
    }

    @Override
    public List<Theme> getAllThemes() {
        return themeRepo.findAllThemes();
    }

    @Override
    public List<SubTheme> getAllSubThemes() {
        return themeRepo.findAllSubThemes();
    }

    @Override
    public List<SubTheme> getSubThemesByThemeId(long id){
        return themeRepo.findSubThemesByThemeId(id);
    }

    @Override
    public List<Card> getCardsByThemeId(long themeId) {
        return themeRepo.findCardsByThemeId(themeId);
    }

    @Override
    public List<Card> getCardsBySubthemeId(long subthemeId) {
        return themeRepo.findCardsBySubthemeId(subthemeId);
    }

    @Override
    public Card getCardById(long cardId) {
        Card card = themeRepo.findCardById(cardId);
        return card;
    }

    @Override
    public Card addCardBySubtheme(Card card,long subThemeId) {
        SubTheme subThemeForCard = themeRepo.findSubThemeById(subThemeId);
        subThemeForCard.addCard(card);
        card.addSubTheme(subThemeForCard);
        themeRepo.editSubTheme(subThemeForCard);
        return themeRepo.createCard(card);
    }

    @Override
    public Card editCard(long cardId,Card card) {
        Card cardToUpdate = themeRepo.findCardById(cardId);
        cardToUpdate.setName(card.getName());
        cardToUpdate.setDescription(card.getDescription());
        cardToUpdate.setDefaultCard(card.isDefaultCard());
        cardToUpdate.setSubThemes(card.getSubThemes());
        return themeRepo.saveCard(cardToUpdate);
    }

    @Override
    public Card removeCardById(long cardId) {
        Card card = this.getCardById(cardId);
        return themeRepo.delete(card);
    }

    //GET-METHODS
    //EDIT-METHODS
    @Override
    public Theme editTheme(Theme theme) {
        if (checkNameLength(theme)) return themeRepo.editTheme(theme);
        throw new InputValidationException("Themename too long");
    }

    @Override
    public SubTheme editSubtheme(SubTheme subTheme) {
        return themeRepo.editSubTheme(subTheme);
    }

    //EDIT-METHODS
    //REMOVE-METHODS

    public void removeAllThemes(){
        themeRepo.deleteAll();
    }

    @Override
    public Theme removeThemeById(long themeId) {
        Theme themeToDelete = themeRepo.findThemeById(themeId);
        for (SubTheme s :themeRepo.findAllSubThemes()){
            if(s.getTheme().getThemeId()==themeId){
                themeRepo.deleteSubTheme(s);
            }
        }
        return themeRepo.deleteTheme(themeToDelete);
    }

    @Override
    public SubTheme removeSubThemeById(long subThemeId){
        SubTheme subThemeToDelete = themeRepo.findSubThemeById(subThemeId);
        return themeRepo.deleteSubTheme(subThemeToDelete);
    }

    @Override
    public List<SubTheme> removeSubThemesByThemeId(long themeId) {
        Theme theme = themeRepo.findThemeById(themeId);
        List<SubTheme> subThemes = themeRepo.findAllSubThemes();
        List<SubTheme> deletedSubThemes= new ArrayList<>();
        for (SubTheme st:subThemes
             ) {
            if(st.getTheme().getThemeId()==themeId){
                deletedSubThemes.add(st);
                themeRepo.deleteSubTheme(st);
            }
        }
        return deletedSubThemes;
    }
    //REMOVE-METHODS

    /**
     * Checks if the themeName is not longer than 50 characters
     * @param theme
     * @return boolean
     */
    private boolean checkNameLength(Theme theme) {
        if(theme.getName().length()<=50){
            return true;
        }
        return false;
    }
}
