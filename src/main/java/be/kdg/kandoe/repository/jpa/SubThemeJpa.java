package be.kdg.kandoe.repository.jpa;

import be.kdg.kandoe.domain.theme.Theme;
import be.kdg.kandoe.dto.converter.DtoConverter;
import be.kdg.kandoe.repository.jpa.converter.JpaConverter;
import com.fasterxml.jackson.annotation.JsonManagedReference;

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
    @JoinColumn(name = "subtheme_themeId")
    private ThemeJpa theme;

    @Column
    private String subThemeName;

    @Column
    private String subThemeDescription;

    @ManyToMany(cascade= CascadeType.ALL,mappedBy = "subThemes")
    @JsonManagedReference
    private List<CardJpa> cards;

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

    public List<CardJpa> getCards() {
        return cards;
    }

    public void setCards(List<CardJpa> cards) {
        this.cards = cards;
    }
}
