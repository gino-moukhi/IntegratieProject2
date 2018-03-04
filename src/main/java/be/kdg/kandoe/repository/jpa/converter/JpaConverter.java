package be.kdg.kandoe.repository.jpa.converter;

import be.kdg.kandoe.domain.theme.Card;
import be.kdg.kandoe.domain.theme.SubTheme;
import be.kdg.kandoe.domain.theme.Theme;
import be.kdg.kandoe.dto.converter.DtoConverter;
import be.kdg.kandoe.repository.jpa.CardJpa;
import be.kdg.kandoe.repository.jpa.SubThemeJpa;
import be.kdg.kandoe.repository.jpa.ThemeJpa;
import be.kdg.kandoe.service.exception.ConversionException;

import java.util.List;
import java.util.stream.Collectors;

public abstract class JpaConverter {
    public static Theme toTheme(ThemeJpa jpa){
        if(jpa==null){
            return null;
        }
        if(!jpa.getClass().equals(ThemeJpa.class)){
            throw new ConversionException("Parameter is not correct Class for conversion");
        }
        Theme theme = new Theme();
        theme.setThemeId(jpa.getThemeId());
        theme.setDescription(jpa.getDescription());
        theme.setName(jpa.getName());
        return theme;
    }

    public static ThemeJpa toThemeJpa(Theme theme){
        if(theme==null){
            return null;
        }
        if(!theme.getClass().equals(Theme.class)){
            throw new ConversionException("Parameter is not correct Class for conversion");
        }
        ThemeJpa jpa = new ThemeJpa();
        jpa.setName(theme.getName());
        jpa.setDescription((theme.getDescription()));
        jpa.setThemeId(theme.getThemeId());
        return jpa;
    }

    public static SubTheme toSubTheme(SubThemeJpa jpa,boolean callByConverter){
        if(jpa==null){
            return null;
        }
        if(!jpa.getClass().equals(SubThemeJpa.class)){
            throw new ConversionException("Parameter is not correct Class for conversion");
        }
        SubTheme subTheme = new SubTheme();
        subTheme.setTheme(JpaConverter.toTheme(jpa.getTheme()));
        subTheme.setSubThemeId(jpa.getSubThemeId());
        subTheme.setSubThemeName(jpa.getSubThemeName());
        subTheme.setSubThemeDescription(jpa.getSubThemeDescription());
        if(!callByConverter){
            if(jpa.getCards()!=null){
                subTheme.setCards(jpa.getCards().stream().map(c-> JpaConverter.toCard(c,true)).collect(Collectors.toList()));
            }
        }

        return subTheme;
    }

    public static SubThemeJpa toSubThemeJpa(SubTheme subTheme,boolean callByConverter){
        if(subTheme==null){
            return null;
        }
        if(!subTheme.getClass().equals(SubTheme.class)){
            throw new ConversionException("Parameter is not correct Class for conversion");
        }
        SubThemeJpa jpa = new SubThemeJpa();
        jpa.setSubThemeId(subTheme.getSubThemeId());
        jpa.setSubThemeName(subTheme.getSubThemeName());
        jpa.setSubThemeDescription(subTheme.getSubThemeDescription());
        jpa.setTheme(JpaConverter.toThemeJpa(subTheme.getTheme()));
        if(!callByConverter){
            if(subTheme.getCards()!=null){
                jpa.setCards(subTheme.getCards().stream().map(c->JpaConverter.toCardJpa(c,true)).collect(Collectors.toList()));
            }
        }
        return jpa;
    }

    public static Card toCard(CardJpa cardJpa, boolean callByConverter){
        if(cardJpa==null){
            return null;
        }
        if(!cardJpa.getClass().equals(CardJpa.class)){
            throw new ConversionException("Parameter is not correct Class for conversion");
        }
        Card newCard = new Card();
        newCard.setCardId(cardJpa.getCardId());
        newCard.setName(cardJpa.getName());
        newCard.setDescription(cardJpa.getDescription());
        newCard.setDefaultCard(cardJpa.isDefaultCard());
        if(!callByConverter){
            newCard.setSubThemes(cardJpa.getSubThemes().stream().map(st->JpaConverter.toSubTheme(st,true)).collect(Collectors.toList()));
        }
        return newCard;
    }

    public static CardJpa toCardJpa(Card card,boolean callByConverter){
        if(card==null){
            return null;
        }
        if(!card.getClass().equals(Card.class)){
            throw new ConversionException("Parameter is not correct Class for conversion");
        }
        CardJpa newJpa = new CardJpa();
        newJpa.setCardId(card.getCardId());
        newJpa.setName(card.getName());
        newJpa.setDescription(card.getDescription());
        newJpa.setDefaultCard(card.isDefaultCard());
        if(!callByConverter){
            newJpa.setSubThemes(card.getSubThemes().stream().map(st->JpaConverter.toSubThemeJpa(st,true)).collect(Collectors.toList()));
        }
        return newJpa;
    }
}
