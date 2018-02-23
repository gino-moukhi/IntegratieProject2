package be.kdg.kandoe.repository.implementation;

import be.kdg.kandoe.domain.theme.Theme;
import be.kdg.kandoe.dto.DtoConverter;
import be.kdg.kandoe.dto.ThemeDto;
import be.kdg.kandoe.repository.declaration.ThemeRepository;
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
import java.util.List;

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
        Query query = em.createQuery("SELECT theme from Theme theme WHERE name = :name",Theme.class).setParameter("name",name);
        System.out.println(query.getResultList().get(0));
        if(query.getResultList()==null || query.getResultList().get(0)==null){
            return null;
        }
        return (Theme)query.getResultList().get(0);
    }

    @Transactional
    @Override
    public Theme findThemeById(@Param("themeId")Long themeId) {
        Query query= em.createQuery("SELECT theme FROM Theme theme WHERE themeId=:themeId",Theme.class).setParameter("themeId",themeId);
        if(query.getResultList().isEmpty() || query.getResultList().get(0)==null){
            return null;
        }
        return (Theme)query.getResultList().get(0);
    }

    @Transactional
    @Override
    public Theme createTheme(Theme theme) {
        em.persist(theme);
        return theme;
    }
    @Transactional
    @Override
    public Theme editTheme(Theme theme) {
        em.persist(theme);
        return theme;
    }

    @Transactional
    @Override
    public Theme deleteTheme(Theme theme){
        em.remove(theme);
        return theme;
    }
    @Transactional
    @Override
    public void deleteAll(){
        em.createQuery("DELETE FROM Theme").executeUpdate();
        em.createNativeQuery("ALTER TABLE THEME ALTER COLUMN theme_id RESTART WITH 1").executeUpdate();
        Query query2 = em.createQuery("SELECT theme FROM Theme theme");
    }

    @Transactional
    @Override
    public List<Theme> findAllThemes() {
        Query query = em.createQuery("SELECT theme FROM Theme theme");
        return query.getResultList();
    }
}

