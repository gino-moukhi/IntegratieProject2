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
    public Theme findThemeByName(@Param("name")String name) {
        Query query = em.createQuery("SELECT theme from ThemeJpa theme WHERE name = :name",ThemeJpa.class).setParameter("name",name);
        System.out.println(query.getResultList().get(0));
        if(query.getResultList()==null || query.getResultList().get(0)==null){
            throw new ThemeRepositoryException("No Theme found by name: "+ name);
        }
        ThemeJpa jpa = (ThemeJpa)query.getResultList().get(0);
        return JpaConverter.toTheme(jpa);
    }

    @Transactional
    @Override
    public Theme findThemeById(@Param("themeId")long themeId) {
        Query query= em.createQuery("SELECT theme FROM ThemeJpa theme WHERE themeId=:themeId",ThemeJpa.class).setParameter("themeId",themeId);
        if(query.getResultList().isEmpty() || query.getResultList().get(0)==null){
            throw new ThemeRepositoryException("No Theme found for ID: "+themeId);
        }
        ThemeJpa jpa = (ThemeJpa)query.getResultList().get(0);
        return JpaConverter.toTheme(jpa);
    }

    @Transactional
    @Override
    public SubTheme findSubThemeById(@Param("subThemeId")long subThemeId){
        Query query= em.createQuery("SELECT subTheme FROM SubThemeJpa subTheme WHERE subThemeId=:subThemeId").setParameter("subThemeId",subThemeId);
        if (query.getResultList().isEmpty()){
            throw new ThemeRepositoryException("No SubTheme found for ID: "+subThemeId);
        }
        SubThemeJpa jpa = (SubThemeJpa)query.getResultList().get(0);
        return JpaConverter.toSubTheme(jpa,false);
    }

    @Transactional
    @Override
    public Theme createTheme(Theme theme) {
        ThemeJpa jpa = JpaConverter.toThemeJpa(theme);
        ThemeJpa response = em.merge(jpa);
        return JpaConverter.toTheme(response);
    }

    @Transactional
    @Override
    public SubTheme createSubTheme(SubTheme subTheme) {
        SubThemeJpa jpa = JpaConverter.toSubThemeJpa(subTheme,false);
        em.merge(jpa);
        return JpaConverter.toSubTheme(jpa,false);
    }

    @Transactional
    @Override
    public Theme editTheme(Theme theme) {
        ThemeJpa jpa = JpaConverter.toThemeJpa(theme);
        em.merge(jpa);
        return JpaConverter.toTheme(jpa);
    }

    @Transactional
    @Override
    public SubTheme editSubTheme(SubTheme subTheme){
        SubThemeJpa jpa = JpaConverter.toSubThemeJpa(subTheme,false);
        em.merge(jpa);
        return JpaConverter.toSubTheme(jpa,false);
    }


    @Transactional
    @Override
    public Theme deleteTheme(Theme theme){
        ThemeJpa jpa = JpaConverter.toThemeJpa(theme);
        em.remove(em.contains(jpa) ?  jpa :em.merge(jpa));
        return JpaConverter.toTheme(jpa);
    }
    @Transactional
    @Override
    public SubTheme deleteSubTheme(SubTheme subTheme){
        SubThemeJpa jpa = JpaConverter.toSubThemeJpa(subTheme,false);
        em.remove(em.contains(jpa) ? jpa:em.merge(jpa));
        return JpaConverter.toSubTheme(jpa,false);
    }

    @Transactional
    @Override
    public void deleteAll(){
        em.createQuery("DELETE from SubThemeJpa").executeUpdate();
        em.createNativeQuery("ALTER TABLE SUBTHEME ALTER COLUMN sub_Theme_id RESTART WITH 1").executeUpdate();
        em.createQuery("DELETE FROM ThemeJpa ").executeUpdate();
        em.createNativeQuery("ALTER TABLE THEME ALTER COLUMN theme_id RESTART WITH 1").executeUpdate();
    }

    @Transactional
    @Override
    public List<Theme> findAllThemes() {
        Query query = em.createQuery("SELECT theme FROM ThemeJpa theme");
        if(query.getResultList().isEmpty()){
            throw new ThemeRepositoryException("No Themes found");
        }
        List<ThemeJpa> jpas = query.getResultList();
        List<Theme> themes = new ArrayList<>();
        for(ThemeJpa jpa:jpas){
            themes.add(JpaConverter.toTheme(jpa));
        }
        return themes;
    }
    @Transactional
    @Override
    public List<SubTheme> findAllSubThemes(){
        Query query= em.createQuery("SELECT subtheme FROM SubThemeJpa subtheme");
        if(query.getResultList().isEmpty()){
            throw new ThemeRepositoryException("No SubThemes found");
        }
        List<SubThemeJpa> jpas = query.getResultList();
        return jpas.stream().map(jpa->JpaConverter.toSubTheme(jpa,false)).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public List<SubTheme> findSubThemesByThemeId(@Param("themeId")long id){
        TypedQuery<SubThemeJpa> query = em.createQuery("SELECT subTheme FROM SubThemeJpa subTheme WHERE theme.themeId=:themeId",SubThemeJpa.class).setParameter("themeId",id);
        if(query.getResultList().isEmpty()){
            throw new ThemeRepositoryException("No SubTheme found for themeId: "+id);
        }
        return query.getResultList().stream().map(jpa->JpaConverter.toSubTheme(jpa,false)).collect(Collectors.toList());
    }

    @Override
    public List<Card> findCardsByThemeId(long themeId) {
        /*<SubThemeJpa> q1 = em.createQuery("SELECT st from SubThemeJpa st where st.theme.themeId=:themeId",SubThemeJpa.class).setParameter("themeId",themeId);
        List<Card> cards=new ArrayList<>();
        for (SubThemeJpa jpa:q1.getResultList()
             ) {
            TypedQuery<Card> q2 = em.createQuery("SELECT c from CardJpa c join SubThemeJpa sub on c.subthemeId = sub.subthemeId where sub.themeId = :themeId", Card.class);
        }

        q.setParameter("themeId",themeId);
        return q.getResultList();*/
        throw new NotImplementedException();
    }

    @Override
    public List<Card> findCardsBySubthemeId(long subthemeId) {
        throw new NotImplementedException();
    }

    @Override
    public Card findCardById(@Param("cardId")long cardId) {
        TypedQuery<CardJpa> q = em.createQuery("SELECT c from CardJpa c where c.cardId = :cardId", CardJpa.class);
        q.setParameter("cardId",cardId);
        if(q.getResultList().isEmpty()){
            throw new ThemeRepositoryException("No Card found for ID: "+cardId);
        }
        return JpaConverter.toCard(q.getSingleResult(),false);
    }

    @Override
    public Card createCard(Card card) {
        CardJpa jpa = JpaConverter.toCardJpa(card,false);
        em.persist(jpa);
        return JpaConverter.toCard(jpa,false);
    }

    @Override
    public Card saveCard(Card card) {
        CardJpa jpa = JpaConverter.toCardJpa(card,false);
        return em.merge(card);
    }

    @Override
    public Card delete(Card card) {
        CardJpa jpa = JpaConverter.toCardJpa(card,false);
        em.remove(em.contains(jpa) ?  jpa :em.merge(jpa));
        return JpaConverter.toCard(jpa,false);
    }
}

