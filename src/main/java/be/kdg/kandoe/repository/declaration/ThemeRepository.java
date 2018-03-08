package be.kdg.kandoe.repository.declaration;

import be.kdg.kandoe.domain.theme.Card;
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
    SubTheme findSingleSubThemeByThemeId(long themeId, long subThemeId);

    List<Card> findCardsByThemeId(long themeId);

    List<Card> findCardsBySubthemeId(long subthemeId);

    Card findCardById(long cardId);

    Card createCard(Card card);

    Card saveCard(Card card);

    Card delete(Card card);
}
