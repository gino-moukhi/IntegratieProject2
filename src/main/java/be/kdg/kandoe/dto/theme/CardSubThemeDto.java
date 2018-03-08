package be.kdg.kandoe.dto.theme;

import be.kdg.kandoe.domain.theme.Card;

public class CardSubThemeDto {
    private long cardSubThemeId;
    private CardDto card;
    private SubThemeDto subTheme;

    public CardSubThemeDto(CardDto card, SubThemeDto subTheme) {
        this.card = card;
        this.subTheme = subTheme;
    }
    public CardSubThemeDto(long id,CardDto card, SubThemeDto subTheme) {
        this.cardSubThemeId=id;
        this.card = card;
        this.subTheme = subTheme;
    }

    public CardSubThemeDto(){

    }

    public long getCardSubThemeId() {
        return cardSubThemeId;
    }

    public void setCardSubThemeId(long cardSubThemeId) {
        this.cardSubThemeId = cardSubThemeId;
    }

    public CardDto getCard() {
        return card;
    }

    public void setCard(CardDto card) {
        this.card = card;
    }

    public SubThemeDto getSubTheme() {
        return subTheme;
    }

    public void setSubTheme(SubThemeDto subTheme) {
        this.subTheme = subTheme;
    }
}
