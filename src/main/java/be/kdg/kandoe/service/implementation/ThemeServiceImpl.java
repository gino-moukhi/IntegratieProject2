package be.kdg.kandoe.service.implementation;

import be.kdg.kandoe.domain.theme.Card;
import be.kdg.kandoe.domain.theme.CardSubTheme;
import be.kdg.kandoe.domain.theme.SubTheme;
import be.kdg.kandoe.domain.theme.Theme;
import be.kdg.kandoe.repository.declaration.ThemeRepository;
import be.kdg.kandoe.service.declaration.ThemeService;
import be.kdg.kandoe.service.exception.InputValidationException;
import be.kdg.kandoe.service.exception.ThemeRepositoryException;
import be.kdg.kandoe.service.exception.ThemeServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
@Primary
public class ThemeServiceImpl implements ThemeService {

    private ThemeRepository themeRepo;

    @Autowired
    public ThemeServiceImpl(ThemeRepository repository) {
        this.themeRepo = repository;
    }

    //ADD-METHODS
    @Override
    public Theme addTheme(Theme theme) throws ThemeRepositoryException {
        if (checkNameLength(theme)) {
            return themeRepo.createTheme(theme);
        } else {
            throw new InputValidationException("Theme name too long");
        }
    }

    @Override
    public SubTheme addSubThemeByThemeId(SubTheme subTheme, long themeId) throws ThemeRepositoryException {
        Theme themeToAdd = themeRepo.findThemeById(themeId);
        subTheme.setTheme(themeToAdd);
        SubTheme result = themeRepo.createSubTheme(subTheme);
        return result;
    }

    @Override
    public Card addCard(Card card) throws ThemeRepositoryException {
        return themeRepo.createCard(card);
    }

    @Override
    public SubTheme addCardToSubTheme(long cardId, long subThemeId) {
        SubTheme subThemeForCard = themeRepo.findSubThemeById(subThemeId);
        Card cardToAdd = themeRepo.findCardById(cardId);
        for (CardSubTheme card : subThemeForCard.getCardSubThemes()) {
            if (card.getCard().getCardId() == cardId && card.getSubTheme().getSubThemeId() == subThemeId) {
                throw new ThemeServiceException("Card Already exists in SubTheme");
            }
        }
        CardSubTheme cst = new CardSubTheme(cardToAdd, subThemeForCard);

        CardSubTheme addedCST = themeRepo.createCardSubTheme(cst);
        subThemeForCard.addCard(addedCST);
        cardToAdd.addCardSubTheme(addedCST);
        SubTheme result1 = themeRepo.editSubTheme(subThemeForCard);
        Card card1 = themeRepo.editCard(cardToAdd);
        return themeRepo.findSubThemeById(subThemeId);
    }

    //ADD-METHODS
    //GET-METHODS
    @Override
    public Theme getThemeByName(String name) throws ThemeRepositoryException {
        Theme themeToFind = themeRepo.findThemeByName(name);
        return themeToFind;
    }

    @Override
    public Theme getThemeById(long id) throws ThemeRepositoryException {
        Theme foundTheme = themeRepo.findThemeById(id);
        return themeRepo.findThemeById(id);
    }

    @Override
    public SubTheme getSubThemeById(long subThemeId) throws ThemeRepositoryException {
        SubTheme foundSubTheme = themeRepo.findSubThemeById(subThemeId);
        return foundSubTheme;
    }

    @Override
    public List<Theme> getAllThemes() throws ThemeRepositoryException {
        return themeRepo.findAllThemes();
    }

    @Override
    public List<SubTheme> getAllSubThemes() throws ThemeRepositoryException {
        return themeRepo.findAllSubThemes();
    }

    @Override
    public List<SubTheme> getSubThemesByThemeId(long id) throws ThemeRepositoryException {
        return themeRepo.findSubThemesByThemeId(id);
    }

    @Override
    public List<Card> getAllCards() {
        return themeRepo.findAllCards();
    }

    @Override
    public List<Card> getCardsBySubthemeId(long subthemeId) throws ThemeRepositoryException {
        return themeRepo.findCardsBySubthemeId(subthemeId);
    }

    @Override
    public Card getCardById(long cardId) throws ThemeRepositoryException {
        Card card = themeRepo.findCardById(cardId);
        return card;
    }

    @Override
    public Card editCard(Card card) throws ThemeRepositoryException {
        return themeRepo.editCard(card);
    }

    @Override
    public Card removeCardById(long cardId) throws ThemeRepositoryException {
        Card card = this.getCardById(cardId);
        return themeRepo.delete(card);
    }

    //GET-METHODS
    //EDIT-METHODS
    @Override
    public Theme editTheme(Theme theme) throws ThemeRepositoryException {
        if (checkNameLength(theme)) return themeRepo.editTheme(theme);
        throw new InputValidationException("Themename too long");
    }

    @Override
    public SubTheme editSubtheme(SubTheme subTheme) {
        return themeRepo.editSubTheme(subTheme);
    }

    //EDIT-METHODS
    //REMOVE-METHODS

    public void removeAllThemes() {
        themeRepo.deleteAll();
    }

    @Override
    public Theme removeThemeById(long themeId) throws ThemeRepositoryException {
        try {
            Theme themeToDelete = themeRepo.findThemeById(themeId);
            return themeRepo.deleteTheme(themeToDelete);
        } catch (ThemeRepositoryException e) {
            throw e;
        }


    }

    @Override
    public SubTheme removeSubThemeById(long subThemeId) throws ThemeRepositoryException {
        SubTheme subThemeToDelete = themeRepo.findSubThemeById(subThemeId);
        return themeRepo.deleteSubTheme(subThemeToDelete);
    }

    @Override
    public List<SubTheme> removeSubThemesByThemeId(long themeId) throws ThemeRepositoryException {
        Theme theme = themeRepo.findThemeById(themeId);
        List<SubTheme> subThemes = themeRepo.findAllSubThemes();
        List<SubTheme> deletedSubThemes = new ArrayList<>();
        for (SubTheme st : subThemes
                ) {
            if (st.getTheme().getThemeId() == themeId) {
                deletedSubThemes.add(st);
                themeRepo.deleteSubTheme(st);
            }
        }
        return deletedSubThemes;
    }

    @Override
    public SubTheme removeCardsFromSubTheme(long subThemeId) {
        SubTheme subThemeToEdit = themeRepo.findSubThemeById(subThemeId);
        subThemeToEdit.setCardSubThemes(new ArrayList<>());
        return themeRepo.editSubTheme(subThemeToEdit);
    }
    //REMOVE-METHODS

    /**
     * Checks if the themeName is not longer than 50 characters
     *
     * @param theme
     * @return boolean
     */
    private boolean checkNameLength(Theme theme) {
        if (theme.getName().length() <= 50) {
            return true;
        }
        return false;
    }
}
