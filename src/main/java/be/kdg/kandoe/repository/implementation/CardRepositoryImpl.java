package be.kdg.kandoe.repository.implementation;

import be.kdg.kandoe.domain.card.Card;
import be.kdg.kandoe.domain.user.User;
import be.kdg.kandoe.repository.declaration.CardRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class CardRepositoryImpl implements CardRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Card> findCardsByThemeId(long themeId) {

        TypedQuery<Card> q = em.createQuery("SELECT c from Card c join Subthemes sub on c.subthemeId = sub.subthemeId where sub.themeId = :themeId", Card.class);
        q.setParameter("themeId",themeId);
        return q.getResultList();
    }

    @Override
    public List<Card> findCardsBySubthemeId(long subthemeId) {
        TypedQuery<Card> q = em.createQuery("SELECT c from Card c where c.subthemeId = :subthemeId", Card.class);
        q.setParameter("subthemeId",subthemeId);
        return q.getResultList();
    }

    @Override
    public Card findCardById(long cardId) {
        TypedQuery<Card> q = em.createQuery("SELECT c from Card c where c.cardId = :cardId", Card.class);
        q.setParameter("cardId",cardId);
        return q.getSingleResult();
    }

    @Override
    public Card createCard(Card card) {
        em.persist(card);
        return card;
    }

    @Override
    public Card saveCard(Card card) {
return em.merge(card);
    }

    @Override
    public Card delete(Card card) {
    em.remove(card);
    return card;
    }
}
