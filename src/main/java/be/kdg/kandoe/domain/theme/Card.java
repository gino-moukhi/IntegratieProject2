package be.kdg.kandoe.domain.theme;

import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Card {
    private long cardId;
    private String name;
    private String description;
    //private MultipartFile image;
    //private String imagePath;
    private boolean isDefaultCard;
    private List<SubTheme> subThemes;

    public Card(){
        subThemes= new ArrayList<>();
    }

    public List<SubTheme> getSubThemes() {
        return this.subThemes;
    }

    public void setSubThemes(List<SubTheme> subThemes) {
        this.subThemes = subThemes;
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

    /*public MultipartFile getImage() {
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
    }*/

    public boolean isDefaultCard() {
        return isDefaultCard;
    }

    public void setDefaultCard(boolean defaultCard) {
        isDefaultCard = defaultCard;
    }

    public void addSubTheme(SubTheme subTheme){
        if(subTheme!=null){
            subThemes.add(subTheme);
        }
    }
    public void removeSubTheme(SubTheme subTheme){
        if(subThemes.contains(subTheme)){
            subThemes.remove(subTheme);
        }
    }

}
