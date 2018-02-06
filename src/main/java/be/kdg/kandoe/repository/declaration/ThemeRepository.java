package be.kdg.kandoe.repository.declaration;

import be.kdg.kandoe.domain.theme.Theme;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThemeRepository {
    Theme findThemeByName(String name);
    Theme findThemeById(Long id);
    Theme createTheme(Theme theme);


    Theme editTheme(Theme theme);
}
