package be.kdg.kandoe.service.implementation;

import be.kdg.kandoe.domain.theme.Theme;
import be.kdg.kandoe.repository.declaration.ThemeRepository;
import be.kdg.kandoe.service.declaration.ThemeService;
import be.kdg.kandoe.service.exception.InputValidationException;
import be.kdg.kandoe.service.exception.ThemeServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ThemeServiceImpl implements ThemeService {

    private ThemeRepository themeRepo;

    @Autowired
    public ThemeServiceImpl(ThemeRepository repository){
        this.themeRepo=repository;
    }

    @Override
    public Theme addTheme(Theme theme) {
        if(checkNameLength(theme)) {
           return themeRepo.createTheme(theme);
        } else {
            throw new InputValidationException("Theme name too long");
        }
    }
    @Override
    public Theme getThemeByName(String name) {
        Theme themeToDelete = themeRepo.findThemeByName(name);
        if(themeToDelete==null){
            throw new ThemeServiceException("No theme found for name: "+name);
        }
        return themeRepo.deleteTheme(themeToDelete);
    }

    @Override
    public Theme getThemeById(long id) {
        Theme foundTheme = themeRepo.findThemeById(id);
        if(foundTheme==null){
            throw new ThemeServiceException("No theme found by id: "+id);
        }
        return themeRepo.findThemeById(id);
    }

    @Override
    public Theme editTheme(Theme theme) {
        if (checkNameLength(theme)) return themeRepo.editTheme(theme);
        throw new InputValidationException("Themename too long");
    }
    @Override
    public Theme removeTheme(Theme themeToDelete) {
        Theme theme = getThemeById(themeToDelete.getThemeId());
        if(theme==null){
            throw new ThemeServiceException("No Theme found for ID: "+themeToDelete.getThemeId());
        }
        return themeRepo.deleteTheme(themeToDelete);
    }

    public void removeAllThemes(){
        themeRepo.deleteAll();
    }

    @Override
    public Theme removeThemeById(long themeId) {
        Theme themeToDelete = themeRepo.findThemeById(themeId);
        if(themeToDelete==null){
            throw new ThemeServiceException("No Theme found for ID: "+themeId);
        }
        return themeRepo.deleteTheme(themeToDelete);
    }

    @Override
    public List<Theme> getAllThemes() {
        return themeRepo.findAllThemes();
    }

    /**
     * Checks if the themeName is not longer than 50 characters
     * @param theme
     * @return boolean
     */
    private boolean checkNameLength(Theme theme) {
        if(theme.getName().length()<=50){
            return true;
        }
        return false;
    }
}
