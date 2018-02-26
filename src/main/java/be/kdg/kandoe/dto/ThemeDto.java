package be.kdg.kandoe.dto;

import be.kdg.kandoe.domain.theme.Theme;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;

/**
 * Mediate Object-class to translate JSON-objects to Theme-objects
 */

public class ThemeDto {

    private long themeId;
    private String name;
    private String description;

    public ThemeDto(){
        this.themeId=0;
        this.name="default";
        this.description="default";
    }

    public ThemeDto(long themeId, String name, String description) {
        this.themeId=themeId;
        this.name = name;
        this.description = description;
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

    public void setThemeId(Long themeId){
        this.themeId=themeId;
    }

    public String toJsonString(){
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"themeId\": \""+this.getThemeId()+"\",");
        sb.append("\"name\": \""+this.getName()+"\",");
        sb.append("\"description\": \""+this.getDescription()+"\",");
        sb.append("}");
        return sb.toString();
    }
    public Theme toTheme(){
        Theme theme = new Theme();
        theme.setDescription(this.description);
        theme.setThemeId(this.themeId);
        theme.setName(this.getName());
        return theme;
    }

    public static ThemeDto fromTheme(Theme theme){
        return new ThemeDto(theme.getThemeId(),theme.getName(),theme.getDescription());
    }
}
