package be.kdg.kandoe.domain.card;

import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;

public class Card {
    private long cardId;
    private String name;
    private String description;
    private MultipartFile image;
    private String imagePath;
    private boolean isDefaultCard;
    private int subthemeId;

    public int getSubthemeId() {
        return subthemeId;
    }

    public void setSubthemeId(int subthemeId) {
        this.subthemeId = subthemeId;
    }

    public long getCardId() {
        return cardId;
    }

    public void setCardId(long cardId) {
        this.cardId = cardId;
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

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
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
