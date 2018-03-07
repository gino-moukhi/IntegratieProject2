package be.kdg.kandoe.repository.jpa;

import be.kdg.kandoe.domain.theme.Theme;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "theme")
public class ThemeJpa {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "theme_id")
    private long themeId;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column
    @OneToMany(targetEntity = SubThemeJpa.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "theme")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    @Fetch(FetchMode.SELECT)
    private List<SubThemeJpa> subthemes;

    public ThemeJpa() {

    }

    public ThemeJpa(Theme theme) {
        this.themeId = theme.getThemeId();
        this.name = theme.getName();
        this.description = theme.getDescription();
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

    /**public List<SubThemeJpa> getSubThemes() {
     return subThemes;
     }**/

    public List<SubThemeJpa> getSubthemes() {
        return subthemes;
    }

    public void setSubthemes(List<SubThemeJpa> subthemes) {
        this.subthemes = subthemes;
    }
}
