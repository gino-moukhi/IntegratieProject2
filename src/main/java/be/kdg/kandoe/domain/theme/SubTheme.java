package be.kdg.kandoe.domain.theme;

import be.kdg.kandoe.dto.theme.SubThemeDto;

import java.util.List;

public class SubTheme {

    private long subThemeId;
    private Theme theme;
    private String subThemeName;
    private String subThemeDescription;
    private List<Card> cards;

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

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public void addCard(Card card){
        this.cards.add(card);
    }
    public void removeCard(Card card){
        if(cards.contains(card)){
            cards.remove(card);
        }
    }
}
