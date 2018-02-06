package be.kdg.kandoe.repository.implementation;

import be.kdg.kandoe.domain.theme.Theme;
import be.kdg.kandoe.repository.declaration.ThemeRepository;
import org.hibernate.jpa.internal.EntityManagerImpl;

import javax.persistence.EntityManager;

public class ThemeRepositoryImpl implements ThemeRepository {
    private EntityManager em;


    public ThemeRepositoryImpl(EntityManager entityManager){
        em = entityManager;
    }

    @Override
    public Theme findThemeByName(String name) {
        return null;
    }

    @Override
    public Theme findThemeById(Long id) {
        return null;
    }

    @Override
    public Theme createTheme(Theme theme) {
        return null;
    }

    @Override
    public Theme editTheme(Theme theme) {
        return null;
    }
}
