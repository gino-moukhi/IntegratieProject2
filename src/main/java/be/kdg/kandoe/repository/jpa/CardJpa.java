package be.kdg.kandoe.repository.jpa;

import be.kdg.kandoe.domain.theme.SubTheme;
import be.kdg.kandoe.dto.converter.DtoConverter;
import be.kdg.kandoe.dto.theme.SubThemeDto;
import be.kdg.kandoe.repository.jpa.converter.JpaConverter;
import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "CARD")
public class CardJpa {
    @Column(name = "cardId")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long cardId;
    private String name;
    private String description;
    //private String imagePath;
    //private byte[] image;
    private boolean isDefaultCard;
    @ManyToMany(cascade = CascadeType.ALL)
    @JsonBackReference
    @JoinTable(
            name="subtheme_card",
            joinColumns = {@JoinColumn(name="cardId")},
            inverseJoinColumns = {@JoinColumn(name = "subthemeId")}
    )
    private List<SubThemeJpa> subThemes;

    public CardJpa() {
        this.subThemes = new ArrayList<>();
    }

    public long getCardId() {
        return cardId;
    }

    public void setCardId(long id){
        this.cardId=id;
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

    /*public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }*/

    public boolean isDefaultCard() {
        return isDefaultCard;
    }

    public void setDefaultCard(boolean defaultCard) {
        isDefaultCard = defaultCard;
    }

    public List<SubThemeJpa> getSubThemes() {
        return this.subThemes;
    }

    public void setSubThemes(List<SubThemeJpa> subThemes) {
        this.subThemes = subThemes;
    }
}