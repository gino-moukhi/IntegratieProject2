package be.kdg.kandoe.repository.jpa;

import be.kdg.kandoe.domain.theme.SubTheme;
import be.kdg.kandoe.domain.theme.Theme;

import javax.persistence.*;

@Entity
@Table(name="SUBTHEME")
public class SubThemeJpa {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private long subThemeId;

    @ManyToOne(targetEntity = ThemeJpa.class,cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "THEME_ID")
    private ThemeJpa theme;

    @Column
    private String subThemeName;

    @Column
    private String subThemeDescription;

    public SubThemeJpa(){

    }
    public SubThemeJpa(SubTheme subTheme){
        this.subThemeId=subTheme.getSubThemeId();
        this.theme=ThemeJpa.fromTheme(subTheme.getTheme());
        this.subThemeName=subTheme.getSubThemeName();
        this.subThemeDescription=subTheme.getSubThemeDescription();
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

    public void setTheme(Theme theme) {
        this.theme = ThemeJpa.fromTheme(theme);
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

    public static SubThemeJpa fromSubTheme(SubTheme subTheme){
        return new SubThemeJpa(subTheme);
    }

    public SubTheme toSubTheme(){
        SubTheme subTheme = new SubTheme();
        subTheme.setTheme(this.theme.toTheme());
        subTheme.setSubThemeId(this.subThemeId);
        subTheme.setSubThemeName(this.subThemeName);
        subTheme.setSubThemeDescription(this.subThemeDescription);
        return subTheme;
    }
}
