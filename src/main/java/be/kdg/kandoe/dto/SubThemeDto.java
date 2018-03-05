package be.kdg.kandoe.dto;

import be.kdg.kandoe.domain.theme.SubTheme;
import be.kdg.kandoe.domain.theme.Theme;
import com.google.gson.Gson;

public class SubThemeDto {
    private Long subThemeId;
    private ThemeDto theme;
    private String subThemeName;
    private String subThemeDescription;

    public SubThemeDto(){

    }

    public SubThemeDto(Long subThemeId,ThemeDto theme,String subThemeName,String subThemeDescription){
        if(subThemeId!=null){
            this.subThemeId=subThemeId;
        }
        this.theme=theme;
        this.subThemeName=subThemeName;
        this.subThemeDescription=subThemeDescription;
    }

    public Long getSubThemeId() {
        return subThemeId;
    }

    public void setSubThemeId(Long subThemeId) {
        this.subThemeId = subThemeId;
    }

    public ThemeDto getTheme() {
        return theme;
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

    public String toJsonString(){
        String JSON = new Gson().toJson(this);
        return JSON;
    }

    public SubTheme toSubTheme(){
        SubTheme subTheme = new SubTheme();
        subTheme.setSubThemeDescription(this.subThemeDescription);
        if(this.subThemeId!=null){
            subTheme.setSubThemeId(this.subThemeId);
        }

        if(this.theme!=null){
            subTheme.setTheme(new Theme(this.theme));
        }
        subTheme.setSubThemeName(this.subThemeName);
        return subTheme;
    }

    public static SubThemeDto fromSubTheme(SubTheme subTheme){
        if(subTheme.getTheme()!=null){
            return new SubThemeDto(subTheme.getSubThemeId(),ThemeDto.fromTheme(subTheme.getTheme()),subTheme.getSubThemeName(),subTheme.getSubThemeDescription());
        }
        return new SubThemeDto(subTheme.getSubThemeId(),null,subTheme.getSubThemeName(),subTheme.getSubThemeDescription());

    }
}
