package be.kdg.kandoe.service.declaration;


import be.kdg.kandoe.domain.theme.Theme;
import org.springframework.stereotype.Service;

@Service
public interface ThemeService {

    void addTheme(Theme theme1);
    Theme getThemeByName(String name);
    Theme getThemeById(long id);

    Theme editTheme(Theme theme);
}
