package be.kdg.kandoe.repository.declaration;

import be.kdg.kandoe.domain.theme.Theme;
import be.kdg.kandoe.dto.ThemeDto;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ThemeRepository {
    Theme findThemeByName(String name);
    Theme findThemeById(Long id);
    Theme createTheme(Theme theme);

    Theme deleteTheme(Theme theme);
    void deleteAll();

    Theme editTheme(Theme theme);

    List<Theme> findAllThemes();
}
