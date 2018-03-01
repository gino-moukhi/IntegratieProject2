package be.kdg.kandoe.dto;

import javax.persistence.*;
import java.awt.image.BufferedImage;

@Entity
@Table(name = "CARDS")
public class CardDto {
    @Column(name = "card_id")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long cardId;
    private String name;
    private String description;
    private String imagePath;
    private byte[] image;
    private boolean isDefaultCard;

    public long getCardId() {
        return cardId;
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

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public boolean isDefaultCard() {
        return isDefaultCard;
    }

    public void setDefaultCard(boolean defaultCard) {
        isDefaultCard = defaultCard;
    }
}
