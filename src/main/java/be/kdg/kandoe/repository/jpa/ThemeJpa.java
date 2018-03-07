package be.kdg.kandoe.repository.jpa;

import be.kdg.kandoe.domain.theme.SubTheme;
import be.kdg.kandoe.domain.theme.Theme;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;
import java.util.stream.Collectors;
import java.util.List;

@Entity
@Table(name = "THEME")
public class ThemeJpa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Generated(GenerationTime.INSERT)
        @Column(name="themeId",nullable = false)
        private long themeId;

        @Column(length = 50,nullable = false)
        private String name;

        @Column(nullable = false)
        private String description;

        @Column
        @OneToMany(targetEntity=SubThemeJpa.class,fetch = FetchType.EAGER,mappedBy = "theme",cascade = CascadeType.REMOVE)
        @OnDelete(action = OnDeleteAction.CASCADE)
        @Fetch(FetchMode.SELECT)
        @JsonIgnore
        private List<SubThemeJpa> subThemes;

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

        public List<SubThemeJpa> getSubThemes() {
            return subThemes;
        }

        public void setSubThemes(List<SubThemeJpa> subThemes){
            this.subThemes=subThemes;
        }

    }
