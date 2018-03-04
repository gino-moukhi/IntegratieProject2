package be.kdg.kandoe.domain.theme;

import be.kdg.kandoe.dto.theme.ThemeDto;

public class Theme {
    private long themeId;
    private String name;
    private String description;


    public Theme(){

    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public long getThemeId() {
        return themeId;
    }
    public void setThemeId(Long id){
        this.themeId=id;
    }

}
