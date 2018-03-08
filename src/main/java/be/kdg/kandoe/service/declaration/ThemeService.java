package be.kdg.kandoe.service.declaration;


import be.kdg.kandoe.domain.theme.Card;
import be.kdg.kandoe.domain.theme.SubTheme;
import be.kdg.kandoe.domain.theme.Theme;
import org.springframework.stereotype.Service;

import java.util.List;


public interface ThemeService {

    Theme addTheme(Theme theme1);
    SubTheme addSubThemeByThemeId(SubTheme subTheme,long themeId);
    Theme getThemeByName(String name);
    Theme getThemeById(long id);
    SubTheme getSubThemeById(long subThemeId);

    Theme editTheme(Theme theme);
    SubTheme editSubtheme(SubTheme subTheme);

    Theme removeThemeById(long themeId);
    SubTheme removeSubThemeById(long subThemeId);
    List<SubTheme> removeSubThemesByThemeId(long themeId);
    void removeAllThemes();

    List<Theme> getAllThemes();
    List<SubTheme> getAllSubThemes();
    List<SubTheme> getSubThemesByThemeId(long id);
    SubTheme getSingleSubThemeByThemeId(long themeId, long subThemeId);


    List<Card> getCardsByThemeId(long themeId);
    List<Card> getCardsBySubthemeId(long subthemeId);
    Card getCardById(long cardId);
    Card addCardBySubtheme(Card card,long subThemeId);
    Card editCard(long cardId,Card card);
    Card removeCardById(long cardId);
}
