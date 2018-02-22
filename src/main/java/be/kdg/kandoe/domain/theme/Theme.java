package be.kdg.kandoe.domain.theme;

import be.kdg.kandoe.dto.ThemeDto;

import javax.persistence.*;

@Entity
@Table(name = "THEME")
public class Theme {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private long themeId;

    @Column(length = 50,nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    public Theme(){

    }
    public Theme(ThemeDto themeDto){
        this.name=themeDto.getName();
        this.description=themeDto.getDescription();
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
}
