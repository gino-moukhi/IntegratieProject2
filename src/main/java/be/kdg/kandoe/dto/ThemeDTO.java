package be.kdg.kandoe.dto;

public class ThemeDTO {
    private long themeId;
    private String name;
    private String description;

    public ThemeDTO(long themeId, String name, String description) {
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

    public void setThemeId(long themeId) {
        this.themeId = themeId;
    }
}
