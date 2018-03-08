package be.kdg.kandoe.domain.theme;

import be.kdg.kandoe.dto.theme.SubThemeDto;

import java.util.List;

public class SubTheme {

    private long subThemeId;
    private Theme theme;
    private String subThemeName;
    private String subThemeDescription;
    private List<CardSubTheme> cardSubThemes;

    public SubTheme(){
    }

    public long getSubThemeId() {
        return subThemeId;
    }

    public void setSubThemeId(long subThemeId) {
        this.subThemeId = subThemeId;
    }

    public Theme getTheme() {
        return theme;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
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

    public List<CardSubTheme> getCardSubThemes() {
        return cardSubThemes;
    }

    public void setCardSubThemes(List<CardSubTheme> cardSubThemes) {
        this.cardSubThemes = cardSubThemes;
    }

    public void addCard(CardSubTheme cardSubTheme){
        this.cardSubThemes.add(cardSubTheme);
    }
    public void removeCard(CardSubTheme cardSubTheme){
        if(cardSubThemes.contains(cardSubTheme)){
            cardSubThemes.remove(cardSubTheme);
        }
    }
}
