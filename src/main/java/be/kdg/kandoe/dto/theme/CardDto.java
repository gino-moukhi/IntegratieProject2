package be.kdg.kandoe.dto.theme;

import be.kdg.kandoe.domain.theme.SubTheme;
import be.kdg.kandoe.dto.converter.DtoConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CardDto {
    private long cardId;
    private String name;
    private String description;
    //private String imagePath;
    //private byte[] image;
    private boolean isDefaultCard;
    private List<SubThemeDto> subThemes;

    public CardDto(){
        subThemes = new ArrayList<>();
    }
    public CardDto(long id, String name, String description, boolean isDefaultCard){
        this.cardId=id;
        this.name=name;
        this.description=description;
        this.isDefaultCard=isDefaultCard;
        this.subThemes=new ArrayList<>();
    }
    public CardDto(long id, String name, String description, boolean isDefaultCard,List<SubThemeDto> subThemes){
        this.cardId=id;
        this.name=name;
        this.description=description;
        this.isDefaultCard=isDefaultCard;
        this.subThemes=subThemes;
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

    public List<SubThemeDto> getSubThemes() {
        return this.subThemes;
    }

    public void setSubThemes(List<SubThemeDto> subThemes) {
        this.subThemes = subThemes;
    }

}
