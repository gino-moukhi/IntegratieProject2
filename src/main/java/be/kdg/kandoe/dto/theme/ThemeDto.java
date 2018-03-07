package be.kdg.kandoe.dto.theme;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Mediate Object-class to translate JSON-objects to Theme-objects
 */

public class ThemeDto {

    private long themeId;
    private String name;
    private String description;
    private List<SubThemeDto> subthemes;

    public ThemeDto(){

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

    public List<SubThemeDto> getSubthemes() {
        return subthemes;
    }

    public void setSubthemes(List<SubThemeDto> subthemes) {
        this.subthemes = subthemes;
    }

    public String toJsonString(){
        String JSON = new Gson().toJson(this);
        return JSON;
    }
}
