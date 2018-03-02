package be.kdg.kandoe.repository.declaration;

import be.kdg.kandoe.domain.card.Card;

import java.util.List;

public interface CardRepository {
    List<Card> findCardsByThemeId(long themeId);

    List<Card> findCardsBySubthemeId(long subthemeId);

    Card findCardById(long cardId);

    Card createCard(Card card);

    Card saveCard(Card card);

    Card delete(Card card);
}
