package be.kdg.kandoe.dto;

import be.kdg.kandoe.domain.theme.SubTheme;
import be.kdg.kandoe.domain.theme.Theme;
import com.google.gson.Gson;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mediate Object-class to translate JSON-objects to Theme-objects
 */

public class ThemeDto {

    private long themeId;
    private String name;
    private String description;
    private List<SubThemeDto> subThemes;

    public ThemeDto(){
        this.themeId=0;
        this.name="default";
        this.description="default";
        this.subThemes= new ArrayList<>();
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

    public List<SubThemeDto> getSubThemes() {
        return subThemes;
    }

    public void setSubThemes(List<SubThemeDto> subThemes){
        this.subThemes=subThemes;
    }

    public void addSubTheme(SubThemeDto dto){
        this.subThemes.add(dto);
    }

    public String toJsonString(){
        String JSON = new Gson().toJson(this);
        return JSON;
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
