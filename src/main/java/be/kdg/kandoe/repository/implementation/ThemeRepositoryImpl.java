package be.kdg.kandoe.repository.implementation;

import be.kdg.kandoe.domain.theme.Theme;
import be.kdg.kandoe.dto.DtoConverter;
import be.kdg.kandoe.dto.ThemeDto;
import be.kdg.kandoe.repository.declaration.ThemeRepository;
import be.kdg.kandoe.repository.jpa.ThemeJpa;
import be.kdg.kandoe.service.exception.ThemeRepositoryException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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
            return null;
        }
        ThemeJpa jpa = (ThemeJpa)query.getResultList().get(0);
        return jpa.toTheme();
    }

    @Transactional
    @Override
    public Theme findThemeById(@Param("themeId")Long themeId) {
        Query query= em.createQuery("SELECT theme FROM ThemeJpa theme WHERE themeId=:themeId",ThemeJpa.class).setParameter("themeId",themeId);
        if(query.getResultList().isEmpty() || query.getResultList().get(0)==null){
            return null;
        }
        ThemeJpa jpa = (ThemeJpa)query.getResultList().get(0);
        return jpa.toTheme();
    }

    @Transactional
    @Override
    public Theme createTheme(Theme theme) {
        ThemeJpa jpa = ThemeJpa.fromTheme(theme);
        em.persist(jpa);
        return jpa.toTheme();
    }
    @Transactional
    @Override
    public Theme editTheme(Theme theme) {
        ThemeJpa jpa = ThemeJpa.fromTheme(theme);
        em.persist(jpa);
        return jpa.toTheme();
    }

    @Transactional
    @Override
    public Theme deleteTheme(Theme theme){
        ThemeJpa jpa = ThemeJpa.fromTheme(theme);
        em.remove(jpa);
        return jpa.toTheme();
    }
    @Transactional
    @Override
    public void deleteAll(){
        em.createQuery("DELETE FROM ThemeJpa ").executeUpdate();
        em.createNativeQuery("ALTER TABLE THEME ALTER COLUMN theme_id RESTART WITH 1").executeUpdate();
    }

    @Transactional
    @Override
    public List<Theme> findAllThemes() {
        Query query = em.createQuery("SELECT theme FROM ThemeJpa theme");
        List<ThemeJpa> jpas = query.getResultList();
        List<Theme> themes = new ArrayList<>();
        for(ThemeJpa jpa:jpas){
            themes.add(jpa.toTheme());
        }
        return themes;
    }
}

