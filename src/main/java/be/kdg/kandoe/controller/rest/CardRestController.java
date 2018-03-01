package be.kdg.kandoe.controller.rest;

import be.kdg.kandoe.domain.card.Card;
import be.kdg.kandoe.service.declaration.CardService;
import be.kdg.kandoe.service.declaration.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
public class CardRestController {
    private final Logger logger = Logger.getLogger(UserRestController.class);
    private final CardService cardService;

    @Autowired
    public CardRestController(CardService cardService) {
        this.cardService = cardService;
    }

    //Get all Cards from theme
    @GetMapping("/api/private/theme/{themeId}/cards")
    public List<Card> getCardsFromTheme(@PathVariable long themeId){
        return cardService.getCardsByThemeId(themeId);
    }

    @GetMapping("/api/private/subtheme/{subthemeId}/cards")
    public List<Card> getCardsFromSubtheme(@PathVariable long subthemeId){
        return cardService.getCardsBySubthemeId(subthemeId);
    }

    @GetMapping("/api/private/card/{cardId}")
    public ResponseEntity<Card> getCard(@PathVariable long cardId){
        Card card = cardService.getCardById(cardId);
        if(card==null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(card);
    }

    @PostMapping("/api/private/subtheme/{subthemeId}/cards")
    public Card createCardBysubtheme(@Valid @RequestBody Card card){
        return cardService.addCardBySubtheme(card);
    }

    @PostMapping("/api/private/card")
    public ResponseEntity<Card> updateCard(@Valid @RequestBody Card card){
        cardService.updateCard(card);
        if(card==null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(card);
    }

    @PostMapping("/api/private/card/{cardId}")
    public ResponseEntity<Card> deleteCardById(@PathVariable long cardId){
        Card card = cardService.removeCardById(cardId);
        if(card==null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(card);
    }
}
