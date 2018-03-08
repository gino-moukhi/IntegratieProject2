package be.kdg.kandoe.dto.converter;

import be.kdg.kandoe.domain.theme.Card;
import be.kdg.kandoe.domain.theme.CardSubTheme;
import be.kdg.kandoe.domain.theme.SubTheme;
import be.kdg.kandoe.domain.theme.Theme;
import be.kdg.kandoe.dto.theme.CardDto;
import be.kdg.kandoe.dto.theme.CardSubThemeDto;
import be.kdg.kandoe.dto.theme.SubThemeDto;
import be.kdg.kandoe.dto.theme.ThemeDto;
import be.kdg.kandoe.service.exception.ConversionException;

import java.util.ArrayList;
import java.util.stream.Collectors;

public abstract class DtoConverter {

    public static ThemeDto toThemeDto(Theme theme,boolean callByConverter){
        if(theme==null){
            return null;
        }
        ThemeDto newDto = new ThemeDto();
        newDto.setThemeId(theme.getThemeId());
        newDto.setName(theme.getName());
        newDto.setDescription(theme.getDescription());
        if(!callByConverter){
            if(theme.getSubThemes()!=null){
                newDto.setSubThemes(theme.getSubThemes().stream().map(st->DtoConverter.toSubThemeDto(st,true)).collect(Collectors.toList()));
            }
            else newDto.setSubThemes(new ArrayList<>());
        }
        return newDto;
    }

    public static Theme toTheme(ThemeDto dto,boolean callByConverter){
        if(dto==null){
            return null;
        }
        Theme theme = new Theme();
        theme.setThemeId(dto.getThemeId());
        theme.setDescription(dto.getDescription());;
        theme.setName(dto.getName());
        if(!callByConverter){
            if(dto.getSubThemes()!=null){
                theme.setSubThemes(dto.getSubThemes().stream().map(st->DtoConverter.toSubTheme(st,true)).collect(Collectors.toList()));
            }else{
                theme.setSubThemes(new ArrayList<>());
            }
        }
        return theme;
    }

    /**
     *
     * @param subTheme
     * @param callByConverter Defines if method is called by Convertor itself, to prevent stackOverflow.
     * @return SubThemeDto of SubTheme
     */
    public static SubThemeDto toSubThemeDto(SubTheme subTheme,boolean callByConverter){
        if(subTheme==null){
            return null;
        }
        SubThemeDto dto = new SubThemeDto();
        dto.setSubThemeId(subTheme.getSubThemeId());
        dto.setSubThemeName(subTheme.getSubThemeName());
        dto.setSubThemeDescription(subTheme.getSubThemeDescription());
        if(!callByConverter){
            if(subTheme.getTheme()!=null){
                dto.setTheme(DtoConverter.toThemeDto(subTheme.getTheme(),true));
            }
            if(subTheme.getCardSubThemes()!=null){
                dto.setCardSubThemes(subTheme.getCardSubThemes().stream().map(c->DtoConverter.toCardSubThemeDto(c)).collect(Collectors.toList()));
            }
        }
        return dto;
    }

    /**
     *
     * @param dto
     * @param callByConverter Defines if method is called by Convertor itself, to prevent stackOverflow.
     * @return SubTheme of SubThemeDto
     */
    public static SubTheme toSubTheme(SubThemeDto dto,boolean callByConverter){
        if(dto==null){
            return null;
        }
        SubTheme subTheme = new SubTheme();
        if(dto.getTheme()!=null){
            subTheme.setTheme(DtoConverter.toTheme(dto.getTheme(),true));
        }
        subTheme.setSubThemeId(dto.getSubThemeId());
        subTheme.setSubThemeName(dto.getSubThemeName());
        subTheme.setSubThemeDescription(dto.getSubThemeDescription());
        if(!callByConverter){
           if(dto.getCardSubThemes()!=null){
               subTheme.setCardSubThemes(dto.getCardSubThemes().stream().map(cst->DtoConverter.toCardSubTheme(cst)).collect(Collectors.toList()));
           }
        }
        return subTheme;
    }


    /**
     *
     * @param cardDto
     * @param callByConverter Defines if method is called by Convertor itself, to prevent stackOverflow.
     * @return Card of CardDto
     */
    public static Card toCard(CardDto cardDto,boolean callByConverter){
        if(cardDto==null){
            return null;
        }
        if(!cardDto.getClass().equals(CardDto.class)){
            throw new ConversionException("Parameter is not correct Class for conversion");
        }
        Card newCard = new Card();
        newCard.setCardId(cardDto.getCardId());
        newCard.setName(cardDto.getName());
        newCard.setDescription(cardDto.getDescription());
        newCard.setDefaultCard(cardDto.isDefaultCard());
        if(!callByConverter){
            if(cardDto.getCardSubThemes()!=null){
                newCard.setCardSubThemes(cardDto.getCardSubThemes().stream().map(cst->DtoConverter.toCardSubTheme(cst)).collect(Collectors.toList()));
            }

        }
        return newCard;
    }

    /**
     *
     * @param card
     * @param callByConverter Defines if method is called by Convertor itself, to prevent stackOverflow.
     * @return CardDto of Card
     */
    public static CardDto toCardDto(Card card,boolean callByConverter){
        if(card==null){
            return null;
        }
        if(!card.getClass().equals(Card.class)){
            throw new ConversionException("Parameter is not correct Class for conversion");
        }
        CardDto dto = new CardDto();
        dto.setCardId(card.getCardId());
        dto.setName(card.getName());
        dto.setDescription(card.getDescription());
        dto.setDefaultCard(card.isDefaultCard());
        if(!callByConverter){
            if(card.getCardSubThemes()!=null){
               dto.setCardSubThemes(card.getCardSubThemes().stream().map(cst->DtoConverter.toCardSubThemeDto(cst)).collect(Collectors.toList()));
            }
        }
        return dto;
    }

    public static CardSubTheme toCardSubTheme(CardSubThemeDto cardSubThemeDto){
        if(cardSubThemeDto==null){
            return null;
        }
        CardSubTheme cst= new CardSubTheme();
        cst.setCardSubThemeId(cardSubThemeDto.getCardSubThemeId());
        cst.setCard(DtoConverter.toCard(cardSubThemeDto.getCard(),true));
        cst.setSubTheme(DtoConverter.toSubTheme(cardSubThemeDto.getSubTheme(),true));
        return cst;
    }

    public static CardSubThemeDto toCardSubThemeDto(CardSubTheme cardSubTheme){
        if(cardSubTheme==null){
            return null;
        }
        CardSubThemeDto dto = new CardSubThemeDto();
        dto.setCardSubThemeId(cardSubTheme.getCardSubThemeId());
        dto.setCard(DtoConverter.toCardDto(cardSubTheme.getCard(),true));
        dto.setSubTheme(DtoConverter.toSubThemeDto(cardSubTheme.getSubTheme(),true));
        return dto;
    }


}
