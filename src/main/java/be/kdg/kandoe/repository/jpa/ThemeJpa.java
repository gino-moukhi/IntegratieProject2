package be.kdg.kandoe.repository.jpa;

import be.kdg.kandoe.domain.theme.SubTheme;
import be.kdg.kandoe.domain.theme.Theme;
import be.kdg.kandoe.dto.ThemeDto;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Entity
    @Table(name = "theme")
    public class ThemeJpa {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "subtheme_id")
        private long themeId;

        @Column(length = 50,nullable = false)
        private String name;

        @Column(nullable = false)
        private String description;


        public ThemeJpa(){

        }
        public ThemeJpa(Theme theme){
            this.themeId=theme.getThemeId();
            this.name=theme.getName();
            this.description=theme.getDescription();
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

        /**public List<SubThemeJpa> getSubThemes() {
            return subThemes;
        }**/

        public Theme toTheme(){
            Theme theme = new Theme();
            theme.setThemeId(this.themeId);
            theme.setDescription(this.description);
            theme.setName(this.name);
            return theme;
        }

         public static ThemeJpa fromTheme(Theme theme){
            ThemeJpa jpa = new ThemeJpa();
            jpa.name=theme.getName();
            jpa.description=(theme.getDescription());
            jpa.themeId=theme.getThemeId();
            return jpa;
        }
    }
