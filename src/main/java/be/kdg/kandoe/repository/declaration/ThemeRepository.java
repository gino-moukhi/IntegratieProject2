package be.kdg.kandoe.repository.declaration;

import be.kdg.kandoe.domain.theme.Card;
import be.kdg.kandoe.domain.theme.CardSubTheme;
import be.kdg.kandoe.domain.theme.SubTheme;
import be.kdg.kandoe.domain.theme.Theme;

import java.util.List;

public interface ThemeRepository {
    Theme findThemeByName(String name);
    Theme findThemeById(long id);
    SubTheme findSubThemeById(long subThemeId);

    Theme createTheme(Theme theme);
    SubTheme createSubTheme(SubTheme subTheme);

    Theme deleteTheme(Theme theme);
    SubTheme deleteSubTheme(SubTheme subTheme);
    void deleteAll();

    Theme editTheme(Theme theme);
    SubTheme editSubTheme(SubTheme subTheme);

    List<Theme> findAllThemes();
    List<SubTheme> findAllSubThemes();
    List<SubTheme> findSubThemesByThemeId(long id);

    List<Card> findCardsBySubthemeId(long subthemeId);

    Card findCardById(long cardId);

    Card createCard(Card card);
    CardSubTheme createCardSubTheme(CardSubTheme cardSubTheme);
    Card delete(Card card);

    Card editCard(Card cardToAdd);

    List<Card> findAllCards();
}
