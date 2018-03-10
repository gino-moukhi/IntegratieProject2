package be.kdg.kandoe.unit.theme;

import be.kdg.kandoe.domain.theme.Card;
import be.kdg.kandoe.domain.theme.CardSubTheme;
import be.kdg.kandoe.domain.theme.SubTheme;
import be.kdg.kandoe.domain.theme.Theme;
import be.kdg.kandoe.repository.declaration.ThemeRepository;
import be.kdg.kandoe.service.exception.ThemeRepositoryException;

import java.util.ArrayList;
import java.util.List;

public class ThemeRepoMock implements ThemeRepository {
    List<Theme> themes = new ArrayList<>();
    List<SubTheme> subThemes = new ArrayList<>();
    List<CardSubTheme> cardSubThemes = new ArrayList<>();
    List<Card> cards = new ArrayList<>();

    public Theme findThemeByName(String name) {
        for (Theme t : themes
                ) {
            if (t.getName().equals(name)) {
                return t;
            }
        }
        throw new ThemeRepositoryException("No theme found");
    }

    public Theme findThemeById(long id) {
        for (Theme t : themes
                ) {
            if (t.getThemeId() == id) {
                return t;
            }
        }
        throw new ThemeRepositoryException("No theme found");
    }

    public SubTheme findSubThemeById(long id) {
        for (SubTheme st : subThemes
                ) {
            if (st.getSubThemeId() == (id)) {
                return st;
            }
        }
        throw new ThemeRepositoryException("No subtheme found");
    }

    @Override
    public CardSubTheme createCardSubTheme(CardSubTheme cardSubTheme) {
        cardSubThemes.add(cardSubTheme);
        return cardSubThemes.get(cardSubThemes.indexOf(cardSubTheme));
    }

    public Theme createTheme(Theme theme) {
        Long size = Long.parseLong(String.valueOf(themes.size() + 1));
        theme.setThemeId(size);
        themes.add(theme);
        return themes.get(themes.indexOf(theme));
    }

    @Override
    public SubTheme createSubTheme(SubTheme subTheme) {
        subThemes.add(subTheme);
        return subThemes.get(subThemes.indexOf(subTheme));
    }

    public Theme editTheme(Theme theme) {
        Theme themeToFind = null;
        for (Theme t : themes
                ) {
            if (t.getThemeId() == theme.getThemeId()) {
                t.setName(theme.getName());
                t.setDescription(theme.getDescription());
                themeToFind = t;
            }
        }
        if (themeToFind == null) {
            return null;
        }
        return themeToFind;
    }

    @Override
    public SubTheme editSubTheme(SubTheme subTheme) {
        for (SubTheme st : subThemes
                ) {
            if (st.getSubThemeId() == subTheme.getSubThemeId()) {
                st.setTheme(subTheme.getTheme());
                st.setCardSubThemes(subTheme.getCardSubThemes());
                st.setSubThemeName(subTheme.getSubThemeName());
                st.setSubThemeDescription(st.getSubThemeDescription());
                return st;
            }
        }
        throw new ThemeRepositoryException("Could not edit subTheme: " + subTheme.getSubThemeId());
    }

    public Theme deleteThemeByName(String name) {
        Theme themeToFind = null;
        for (Theme t : themes
                ) {
            if (t.getName().equals(name)) {
                themeToFind = t;
            }
        }
        if (themeToFind == null) {
            return null;
        }
        themes.remove(themeToFind);
        return themeToFind;
    }


    public Theme deleteThemeByThemeId(Long themeId) {
        Theme themeToFind = null;
        for (Theme t : themes
                ) {
            if (t.getThemeId() == themeId) {
                themeToFind = t;
            }
        }
        if (themeToFind == null) {
            return null;
        }
        themes.remove(themeToFind);
        return themeToFind;
    }

    @Override
    public SubTheme deleteSubTheme(SubTheme subTheme) {
        SubTheme stToDelete = null;
        for (SubTheme st : subThemes
                ) {
            if (st.equals(subTheme)) {
                stToDelete = st;
            }
        }
        subThemes.remove(stToDelete);
        return stToDelete;
    }

    @Override
    public Theme deleteTheme(Theme theme) {
        themes.remove(theme);
        return theme;
    }

    @Override
    public void deleteAll() {
        themes = new ArrayList<>();
    }

    @Override
    public List<Theme> findAllThemes() {
        if (themes != null) {
            return themes;
        } else return null;
    }

    @Override
    public List<SubTheme> findAllSubThemes() {
        if (themes != null) {
            return subThemes;
        }
        return null;
    }

    @Override
    public List<SubTheme> findSubThemesByThemeId(long id) {
        List<SubTheme> subThemes = new ArrayList<>();
        for (SubTheme st : this.subThemes) {
            if (st.getTheme().getThemeId() == id) {
                subThemes.add(st);
            }
        }
        return subThemes;
    }

    @Override
    public SubTheme findSingleSubThemeByThemeId(long themeId, long subThemeId) {
        for (Theme t :
                themes) {
            if (t.getThemeId() == themeId) {
                for (SubTheme st :
                        subThemes) {
                    if (st.getSubThemeId() == subThemeId) {
                        return st;
                    }
                }
                throw new ThemeRepositoryException("No subtheme found for theme: "+t.getThemeId());
            }
        }
        throw new ThemeRepositoryException("No theme found with ID: "+ themeId);
    }

    /*@Override
    public List<Card> findCardsByThemeId(long themeId) {
        return null;
    }
*/
    @Override
    public List<Card> findCardsBySubthemeId(long subthemeId) {
        List<Card> foundCards = new ArrayList<>();
        for (Card c : cards
                ) {
            for (CardSubTheme st : c.getCardSubThemes()
                    ) {
                if (st.getSubTheme().getSubThemeId() == subthemeId) {
                    if (!foundCards.contains(c)) foundCards.add(c);
                }
            }
        }
        return foundCards;
    }

    @Override
    public Card findCardById(long cardId) {
        for (Card card : cards) {
            if (card.getCardId() == cardId) {
                return card;
            }
        }
        throw new ThemeRepositoryException("no card found");
    }

    @Override
    public Card createCard(Card card) {
        cards.add(card);
        return card;
    }

    @Override
    public Card delete(Card card) {
        cards.remove(card);
        return card;
    }

    @Override
    public Card editCard(Card cardToEdit) {
        for (Card c : cards) {
            if (c.getCardId() == cardToEdit.getCardId()) {
                c.setCardSubThemes(cardToEdit.getCardSubThemes());
                c.setName(cardToEdit.getName());
                c.setDescription(cardToEdit.getDescription());
                c.setDefaultCard(cardToEdit.isDefaultCard());
                return c;
            }
        }
        throw new ThemeRepositoryException("Could not edit theme: " + cardToEdit.getCardId());
    }

    @Override
    public List<Card> findAllCards() {
        return this.cards;
    }

    public void setThemes(ArrayList<Theme> themes) {
        this.themes = themes;
    }

    public void setSubThemes(ArrayList<SubTheme> subThemes) {
        this.subThemes = subThemes;
    }

    public void setCards(ArrayList<Card> cards) {
        this.cards = cards;
    }

}
