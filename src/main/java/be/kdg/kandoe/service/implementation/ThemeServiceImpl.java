package be.kdg.kandoe.service.implementation;

import be.kdg.kandoe.domain.theme.Theme;
import be.kdg.kandoe.repository.declaration.ThemeRepository;
import be.kdg.kandoe.service.declaration.ThemeService;
import be.kdg.kandoe.service.exception.InputValidationException;


public class ThemeServiceImpl implements ThemeService {

    private ThemeRepository themeRepo;

    public ThemeServiceImpl(ThemeRepository themeRepo){
        this.themeRepo = themeRepo;
    }

    @Override
    public void addTheme(Theme theme1) {
        if(theme1.getName().length() <= 50) {
            themeRepo.createTheme(theme1);
        } else {
            throw new InputValidationException("Theme name too long");
        }
    }

    @Override
    public Theme getThemeByName(String name) {
        return themeRepo.findThemeByName(name);
    }

    @Override
    public Theme getThemeById(long id) {
        return themeRepo.findThemeById(id);
    }

    @Override
    public Theme editTheme(Theme theme) {
        if(theme.getName().length()<=50){
            return themeRepo.editTheme(theme);
        }
        throw new InputValidationException("Themename too long");
    }
}
