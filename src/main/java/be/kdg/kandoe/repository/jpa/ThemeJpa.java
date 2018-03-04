package be.kdg.kandoe.repository.jpa;

import be.kdg.kandoe.domain.theme.Theme;

import javax.persistence.*;

@Entity
    @Table(name = "theme")
    public class ThemeJpa {
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        @Column(name="theme_id")
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
        public void setThemeId(long themeId){
            this.themeId=themeId;
        }

        /**public List<SubThemeJpa> getSubThemes() {
            return subThemes;
        }**/

    }
