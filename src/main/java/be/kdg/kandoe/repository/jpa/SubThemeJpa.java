package be.kdg.kandoe.repository.jpa;

import be.kdg.kandoe.domain.theme.SubTheme;
import be.kdg.kandoe.domain.theme.Theme;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;

@Entity
@Table(name="SUBTHEME")
public class SubThemeJpa {
    @Id
    @Generated(GenerationTime.INSERT)
    @Column(name = "subtheme_id",columnDefinition = "serial")
    private Long subThemeId;

    @ManyToOne(targetEntity = ThemeJpa.class,cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "subtheme_theme_id")
    private ThemeJpa theme;

    @Column
    private String subThemeName;

    @Column
    private String subThemeDescription;

    public SubThemeJpa(){

    }
    public SubThemeJpa(SubTheme subTheme){
        this.subThemeId=subTheme.getSubThemeId();
        if(subTheme.getTheme()!=null){
            this.theme=ThemeJpa.fromTheme(subTheme.getTheme());
        }
        this.subThemeName=subTheme.getSubThemeName();
        this.subThemeDescription=subTheme.getSubThemeDescription();
    }

    public Long getSubThemeId() {
        return subThemeId;
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
        if(this.theme!=null){
            subTheme.setTheme(this.theme.toTheme());
        }
        if(this.subThemeId!=null){
            subTheme.setSubThemeId(this.subThemeId);
        }
        subTheme.setSubThemeName(this.subThemeName);
        subTheme.setSubThemeDescription(this.subThemeDescription);
        return subTheme;
    }
}
