package be.kdg.kandoe.repository.implementation;

import be.kdg.kandoe.domain.theme.Theme;
import be.kdg.kandoe.repository.declaration.ThemeRepository;
import org.hibernate.jpa.internal.EntityManagerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class ThemeRepositoryImpl implements ThemeRepository {
    private EntityManager em;

    @Autowired
    public ThemeRepositoryImpl(EntityManager entityManager){
        em = entityManager;
    }

    @Override
    public Theme findThemeByName(String name) {
        throw new NotImplementedException();
    }

    @Override
    public Theme findThemeById(Long id) {
        throw new NotImplementedException();
    }

    @Override
    public Theme createTheme(Theme theme) {
        throw new NotImplementedException();
    }

    @Override
    public Theme editTheme(Theme theme) {
        throw new NotImplementedException();
    }

    @Override
    public Theme deleteTheme(Theme themeToDelete) {
        throw new NotImplementedException();
    }

    @Override
    public Theme deleteThemeById(long themeId) {
        throw new NotImplementedException();
    }

    @Override
    public List<Theme> findAllThemes() {
        throw new NotImplementedException();
    }
}
