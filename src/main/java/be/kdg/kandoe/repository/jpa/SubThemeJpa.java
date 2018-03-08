package be.kdg.kandoe.repository.jpa;

import be.kdg.kandoe.domain.theme.Theme;
import be.kdg.kandoe.dto.converter.DtoConverter;
import be.kdg.kandoe.repository.jpa.converter.JpaConverter;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="SUBTHEME")
public class SubThemeJpa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subthemeId")
    private long subThemeId;

    @ManyToOne(targetEntity = ThemeJpa.class,cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "themeId_PK")
    private ThemeJpa theme;

    @Column
    private String subThemeName;

    @Column
    private String subThemeDescription;

    @Column
    @OneToMany(targetEntity = CardSubThemeJpa.class,cascade = CascadeType.ALL,fetch = FetchType.EAGER,mappedBy = "subTheme")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<CardSubThemeJpa> cardSubThemes;

    public SubThemeJpa(){

    }

    public Long getSubThemeId() {
        return subThemeId;
    }

    public void setSubThemeId(Long subThemeId) {
        this.subThemeId = subThemeId;
    }

    public ThemeJpa getTheme() {
        return this.theme;
    }

    public void setTheme(ThemeJpa theme) {
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

    public List<CardSubThemeJpa> getCardSubThemes() {
        return cardSubThemes;
    }

    public void setCardSubThemes(List<CardSubThemeJpa> cardSubThemes) {
        this.cardSubThemes = cardSubThemes;
    }
}
