package be.kdg.kandoe.dto;

public class SubThemeDto {
    private Long subThemeId;
    private String subThemeName;
    private String subThemeDescription;

    public SubThemeDto(Long subThemeId,String subThemeName,String subThemeDescription){
        this.subThemeId=subThemeId;
        this.subThemeName=subThemeName;
        this.subThemeDescription=subThemeDescription;
    }

    public Long getSubThemeId() {
        return subThemeId;
    }

    public void setSubThemeId(Long subThemeId) {
        this.subThemeId = subThemeId;
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
}
