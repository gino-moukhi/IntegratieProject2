package be.kdg.kandoe.service.implementation;

import be.kdg.kandoe.domain.card.Card;
import be.kdg.kandoe.dto.CardDto;
import be.kdg.kandoe.repository.declaration.CardRepository;
import be.kdg.kandoe.service.declaration.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardServiceImpl implements CardService {
    private CardRepository cardRepository;

    @Autowired
    public CardServiceImpl(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    @Override
    public List<Card> getCardsByThemeId(long themeId) {
       return cardRepository.findCardsByThemeId(themeId);
    }

    @Override
    public List<Card> getCardsBySubthemeId(long subthemeId) {
        return cardRepository.findCardsBySubthemeId(subthemeId);
    }

    @Override
    public Card getCardById(long cardId) {
        return cardRepository.findCardById(cardId);
    }

    @Override
    public Card addCardBySubtheme(Card card) {
        return cardRepository.createCard(card);
    }

    @Override
    public Card updateCard(Card card) {
return cardRepository.saveCard(card);
    }

    @Override
    public Card removeCardById(long cardId) {
        Card card = cardRepository.findCardById(cardId);
        if(card!=null) {
            return cardRepository.delete(card);
        }
        return null;
    }
}
