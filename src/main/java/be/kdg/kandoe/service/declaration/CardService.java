package be.kdg.kandoe.service.declaration;

import be.kdg.kandoe.domain.card.Card;

import java.util.List;

public interface CardService {
    List<Card> getCardsByThemeId(long themeId);

    List<Card> getCardsBySubthemeId(long subthemeId);

    Card getCardById(long cardId);

    Card addCardBySubtheme(Card card);

    Card updateCard(Card card);

    Card removeCardById(long cardId);
}
