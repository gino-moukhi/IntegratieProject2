package be.kdg.kandoe.repository.implementation;

import be.kdg.kandoe.domain.theme.Card;
import be.kdg.kandoe.domain.theme.SubTheme;
import be.kdg.kandoe.domain.theme.Theme;
import be.kdg.kandoe.repository.declaration.ThemeRepository;
import be.kdg.kandoe.repository.jpa.CardJpa;
import be.kdg.kandoe.repository.jpa.SubThemeJpa;
import be.kdg.kandoe.repository.jpa.ThemeJpa;
import be.kdg.kandoe.repository.jpa.converter.JpaConverter;
import be.kdg.kandoe.service.exception.ThemeRepositoryException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ThemeRepositoryImpl implements ThemeRepository {

    @PersistenceContext
    private final EntityManager em;

    @Autowired
    public ThemeRepositoryImpl(EntityManager entityManager) {
        em = entityManager;
    }

    @Transactional
    @Override
    public Theme findThemeByName(@Param("name") String name) {
        Query query = em.createQuery("SELECT theme from ThemeJpa theme WHERE name = :name", ThemeJpa.class).setParameter("name", name);
        System.out.println(query.getResultList().get(0));
        if (query.getResultList() == null || query.getResultList().get(0) == null) {
            throw new ThemeRepositoryException("No Theme found by name: " + name);
        }
        ThemeJpa jpa = (ThemeJpa) query.getResultList().get(0);
        return JpaConverter.toTheme(jpa, false);
    }

    @Transactional
    @Override
    public Theme findThemeById(@Param("themeId") long themeId) {
        Query query = em.createQuery("SELECT theme FROM ThemeJpa theme WHERE themeId=:themeId", ThemeJpa.class).setParameter("themeId", themeId);
        if (query.getResultList().isEmpty() || query.getResultList().get(0) == null) {
            throw new ThemeRepositoryException("No Theme found for ID: " + themeId);
        }
        ThemeJpa jpa = (ThemeJpa) query.getResultList().get(0);
        return JpaConverter.toTheme(jpa, false);
    }

    @Transactional
    @Override
    public SubTheme findSubThemeById(@Param("subThemeId") long subThemeId) {
        Query query = em.createQuery("SELECT subTheme FROM SubThemeJpa subTheme WHERE subThemeId=:subThemeId").setParameter("subThemeId", subThemeId);
        if (query.getResultList().isEmpty()) {
            throw new ThemeRepositoryException("No SubTheme found for ID: " + subThemeId);
        }
        SubThemeJpa jpa = (SubThemeJpa) query.getResultList().get(0);
        return JpaConverter.toSubTheme(jpa, false);
    }

    @Transactional
    @Override
    public Theme createTheme(Theme theme) {
        ThemeJpa jpa = JpaConverter.toThemeJpa(theme, false);
        ThemeJpa response = em.merge(jpa);
        return JpaConverter.toTheme(response, false);
    }

    @Transactional
    @Override
    public SubTheme createSubTheme(SubTheme subTheme) {
        SubThemeJpa jpa = JpaConverter.toSubThemeJpa(subTheme, false);
        SubThemeJpa response = em.merge(jpa);
        return JpaConverter.toSubTheme(response, false);
    }

    @Transactional
    @Override
    public Theme editTheme(Theme theme) {
        ThemeJpa jpa = JpaConverter.toThemeJpa(theme, false);
        em.merge(jpa);
        return JpaConverter.toTheme(jpa, false);
    }

    @Transactional
    @Override
    public SubTheme editSubTheme(SubTheme subTheme) {
        SubThemeJpa jpa = JpaConverter.toSubThemeJpa(subTheme, false);
        em.merge(jpa);
        return JpaConverter.toSubTheme(jpa, false);
    }

    @Transactional
    @Override
    public Card editCard(Card card) {
        CardJpa jpa = JpaConverter.toCardJpa(card, false);
        CardJpa result = em.merge(jpa);
        return JpaConverter.toCard(result, false);
    }


    @Transactional
    @Override
    public Theme deleteTheme(Theme theme) {
        ThemeJpa jpa = JpaConverter.toThemeJpa(theme, false);
        em.remove(em.contains(jpa) ? jpa : em.merge(jpa));
        return JpaConverter.toTheme(jpa, false);
    }

    @Transactional
    @Override
    public SubTheme deleteSubTheme(SubTheme subTheme) {
        SubThemeJpa jpa = JpaConverter.toSubThemeJpa(subTheme, false);
        em.remove(em.contains(jpa) ? jpa : em.merge(jpa));
        return JpaConverter.toSubTheme(jpa, false);
    }

    @Transactional
    @Override
    public void deleteAll() {
        em.createQuery("DELETE from SubThemeJpa").executeUpdate();
        em.createQuery("DELETE from CardJpa").executeUpdate();
        em.createQuery("DELETE FROM ThemeJpa ").executeUpdate();
    }

    @Transactional
    @Override
    public List<Theme> findAllThemes() {
        Query query = em.createQuery("SELECT theme FROM ThemeJpa theme");
        if (query.getResultList().isEmpty()) {
            throw new ThemeRepositoryException("No Themes found");
        }
        List<ThemeJpa> jpas = query.getResultList();
        List<Theme> themes = new ArrayList<>();
        for (ThemeJpa jpa : jpas) {
            themes.add(JpaConverter.toTheme(jpa, false));
        }
        return themes;
    }

    @Transactional
    @Override
    public List<SubTheme> findAllSubThemes() {
        Query query = em.createQuery("SELECT subtheme FROM SubThemeJpa subtheme");
        if (query.getResultList().isEmpty()) {
            throw new ThemeRepositoryException("No SubThemes found");
        }
        List<SubThemeJpa> jpas = query.getResultList();
        return jpas.stream().map(jpa -> JpaConverter.toSubTheme(jpa, false)).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public List<SubTheme> findSubThemesByThemeId(@Param("themeId") long id) {
        TypedQuery<SubThemeJpa> query = em.createQuery("SELECT subTheme FROM SubThemeJpa subTheme WHERE subTheme.theme.themeId=:themeId", SubThemeJpa.class).setParameter("themeId", id);
        if (query.getResultList().isEmpty()) {
            throw new ThemeRepositoryException("No SubTheme found for themeId: " + id);
        }
        return query.getResultList().stream().map(jpa -> JpaConverter.toSubTheme(jpa, false)).collect(Collectors.toList());
    }


    @Override
    @Transactional
    public List<Card> findCardsBySubthemeId(long subthemeId) {
        TypedQuery<CardJpa> q = em.createQuery("SELECT card FROM CardJpa card " +
                "join card.subThemes st where st.subThemeId=:subThemeId", CardJpa.class)
                .setParameter("subThemeId", subthemeId);

        if (q.getResultList().isEmpty()) {
            throw new ThemeRepositoryException("No Cards found for SubTheme with ID: " + subthemeId);
        }
        return q.getResultList().stream().map(c -> JpaConverter.toCard(c, false)).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<Card> findAllCards() {
        TypedQuery<CardJpa> query = em.createQuery("SELECT card FROM CardJpa card", CardJpa.class);
        if (query.getResultList().isEmpty()) {
            throw new ThemeRepositoryException("No cards found");
        }
        return query.getResultList().stream().map(c -> JpaConverter.toCard(c, false)).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Card findCardById(@Param("cardId") long cardId) {
        TypedQuery<CardJpa> q = em.createQuery("SELECT c from CardJpa c where c.cardId = :cardId", CardJpa.class);
        q.setParameter("cardId", cardId);
        if (q.getResultList().isEmpty()) {
            throw new ThemeRepositoryException("No Card found for ID: " + cardId);
        }
        return JpaConverter.toCard(q.getSingleResult(), false);
    }

    @Override
    @Transactional
    public Card createCard(Card card) {
        CardJpa jpa = JpaConverter.toCardJpa(card, false);
        em.persist(jpa);
        return JpaConverter.toCard(jpa, false);
    }

    @Override
    @Transactional
    public Card delete(Card card) {
        CardJpa jpa = JpaConverter.toCardJpa(card, false);
        em.remove(em.contains(jpa) ? jpa : em.merge(jpa));
        return JpaConverter.toCard(jpa, false);
    }
}

