package be.kdg.kandoe.dto.theme;

import be.kdg.kandoe.dto.converter.DtoConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class SubThemeDto {
    private long subThemeId;
    private ThemeDto theme;
    private String subThemeName;
    private String subThemeDescription;
    private List<CardSubThemeDto> cards;

    public SubThemeDto() {
        cards = new ArrayList<>();
    }

    public SubThemeDto(long id, String name, String description) {
        this.subThemeId = id;
        this.subThemeName = name;
        this.subThemeDescription = description;
    }

    public SubThemeDto(long id, ThemeDto theme, String name, String description, List<CardSubThemeDto> cards) {
        this.subThemeId = id;
        this.subThemeName = name;
        this.subThemeDescription = description;
        this.theme = theme;
        this.cards = cards;
    }

    public long getSubThemeId() {
        return subThemeId;
    }

    public void setSubThemeId(long subThemeId) {
        this.subThemeId = subThemeId;
    }

    public ThemeDto getTheme() {
        return this.theme;
    }

    public void setTheme(ThemeDto theme) {
        this.theme = theme;
    }

    public String getSubThemeName() {
        return subThemeName;
    }

    public void setSubThemeName(String subThemeName) {
        this.subThemeName = subThemeName;
    }

    public String getSubThemeDescription() {
        return subThemeDescription;
    }

    public void setSubThemeDescription(String subThemeDescription) {
        this.subThemeDescription = subThemeDescription;
    }

    public List<CardSubThemeDto> getCardSubThemes() {
        return this.cards;
    }

    public void setCardSubThemes(List<CardSubThemeDto> cards) {
        this.cards = cards;
    }

    public String toJsonString() {
        String JSON = new Gson().toJson(this);
        return JSON;
    }

}
