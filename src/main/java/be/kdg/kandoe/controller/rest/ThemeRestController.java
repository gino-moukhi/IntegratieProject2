package be.kdg.kandoe.controller.rest;

import be.kdg.kandoe.domain.theme.Card;
import be.kdg.kandoe.domain.theme.SubTheme;
import be.kdg.kandoe.domain.theme.Theme;
import be.kdg.kandoe.dto.converter.DtoConverter;
import be.kdg.kandoe.dto.theme.CardDto;
import be.kdg.kandoe.dto.theme.SubThemeDto;
import be.kdg.kandoe.dto.theme.ThemeDto;
import be.kdg.kandoe.repository.declaration.ThemeRepository;
import be.kdg.kandoe.service.declaration.ThemeService;
import be.kdg.kandoe.service.exception.ThemeRepositoryException;
import be.kdg.kandoe.service.implementation.ThemeServiceImpl;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ThemeRestController {
    private final Logger logger = Logger.getLogger(ThemeRestController.class);

    private ThemeService themeService;

    public ThemeRestController(ThemeRepository repository) {
        this.themeService = new ThemeServiceImpl(repository);
    }

    @RequestMapping(value = "api/public/themes", method = RequestMethod.GET)
    public ResponseEntity<List<ThemeDto>> getAllThemes() {
        System.out.println("CALL RECEIVED: getAllThemes");
        List<Theme> allThemes = themeService.getAllThemes();
        return ResponseEntity.ok().body(allThemes.stream().map(t -> DtoConverter.toThemeDto(t, false)).collect(Collectors.toList()));
    }

    //GET-METHODS

    @RequestMapping(value = "api/public/theme/{themeId}", method = RequestMethod.GET)
    public ResponseEntity<ThemeDto> getThemeById(@PathVariable Long themeId) {
        System.out.println("CALLED RECEIVED: getThemeById: " + themeId);
        Theme theme;
        try {
            theme = themeService.getThemeById(themeId);
        } catch (ThemeRepositoryException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(DtoConverter.toThemeDto(theme, false));
    }

    @RequestMapping(value = "api/public/subthemes", method = RequestMethod.GET)
    public ResponseEntity<List<SubThemeDto>> getAllSubThemes() {
        System.out.println("CALL RECEIVED: getAllSubThemes");
        List<SubTheme> subThemes = themeService.getAllSubThemes();
        return ResponseEntity.ok().body(subThemes.stream().map(st -> DtoConverter.toSubThemeDto(st, false)).collect(Collectors.toList()));
    }

    @RequestMapping(value = "api/public/theme", method = RequestMethod.GET)
    public ResponseEntity<ThemeDto> getThemeByName(@RequestParam(name = "name") String name) {
        System.out.println("CALL RECEIVED: getThemeByName: " + name);
        Theme theme = themeService.getThemeByName(name);
        if (theme != null) {
            return ResponseEntity.ok().body(DtoConverter.toThemeDto(theme, false));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping(value = "api/public/subtheme/{subThemeId}", method = RequestMethod.GET)
    public ResponseEntity<SubThemeDto> getSubThemeById(@PathVariable(name = "subThemeId") long subThemeId) {
        System.out.println("CALL RECEIVED: getSubThemeById: " + subThemeId);
        SubTheme subTheme = themeService.getSubThemeById(subThemeId);
        if (subTheme == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(DtoConverter.toSubThemeDto(subTheme, false));
    }

    @RequestMapping(value = "api/public/theme/{themeId}/subthemes", method = RequestMethod.GET)
    public ResponseEntity<List<SubThemeDto>> getSubThemesByThemeId(@PathVariable(name = "themeId") long themeId) {
        System.out.println("CALL RECEIVED: getSubThemesByThemeId: " + themeId);
        try {
            List<SubTheme> subThemes = themeService.getSubThemesByThemeId(themeId);
            return ResponseEntity.ok().body(subThemes.stream().map(st -> DtoConverter.toSubThemeDto(st, false)).collect(Collectors.toList()));
        } catch (ThemeRepositoryException te) {
            te.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping(value = "api/public/subtheme/{subThemeId}/cards", method = RequestMethod.GET)
    public ResponseEntity<List<CardDto>> getCardsBySubThemeId(@PathVariable(name = "subThemeId") long subThemeId) {
        try {
            List<Card> cards = themeService.getCardsBySubthemeId(subThemeId);
            return ResponseEntity.ok().body(cards.stream().map(c -> DtoConverter.toCardDto(c, false)).collect(Collectors.toList()));
        } catch (ThemeRepositoryException te) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @RequestMapping(value = "api/public/theme/{themeId}/subtheme/{subThemeId}", method= RequestMethod.GET)
    public ResponseEntity<SubThemeDto> getSingleSubThemeByThemeId(@PathVariable(name="themeId")long themeId, @PathVariable(name = "subThemeId")long subThemeId){
        System.out.println("CALL RECEIVED: getSingleSubThemeByThemeId: THEME "+themeId+" SUBTHEME: "+subThemeId);
        SubTheme subTheme = themeService.getSingleSubThemeByThemeId(themeId, subThemeId);
        if(subTheme==null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(DtoConverter.toSubThemeDto(subTheme, false));
    }

    //GET-METHODS
    //POST-METHODS
    @RequestMapping(value = "api/public/themes", method = RequestMethod.POST)
    public ResponseEntity<ThemeDto> CreateTheme(@Valid @RequestBody ThemeDto themeDto) {
        System.out.println("CALL RECEIVED: CreateTheme");
        logger.log(Priority.INFO, "API CALL: CreateTheme");
        Theme createdTheme = themeService.addTheme(DtoConverter.toTheme(themeDto, false));
        if (createdTheme == null) {
            logger.log(Priority.ERROR, "ThemeService.addTheme returns NULL..");
            return ResponseEntity.badRequest().build();
        } else {
            logger.log(Priority.INFO, "Successfully created new Theme");
            return ResponseEntity.ok().body(DtoConverter.toThemeDto(createdTheme, false));
        }
    }

    @RequestMapping(value = "api/public/subthemes/{themeId}", method = RequestMethod.POST)
    public ResponseEntity<SubThemeDto> CreateSubTheme(@Valid @RequestBody SubThemeDto subThemeDto, @PathVariable(name = "themeId") long themeId) {
        System.out.println("CALL RECEICED: CreateSubTheme");
        logger.log(Priority.INFO, "API CALL: CreateSubTheme");
        SubTheme subTheme = DtoConverter.toSubTheme(subThemeDto, false);
        SubTheme createdSubTheme = themeService.addSubThemeByThemeId(DtoConverter.toSubTheme(subThemeDto, false), themeId);
        if (createdSubTheme != null) {
            return ResponseEntity.ok().body(DtoConverter.toSubThemeDto(createdSubTheme, false));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    //POST-METHODS
    //PUT-METHODS
    @RequestMapping(value = "api/public/theme/{themeId}", method = RequestMethod.PUT)
    public ResponseEntity<ThemeDto> updateTheme(@PathVariable long themeId, @Valid @RequestBody ThemeDto theme) {
        System.out.println("CALL RECEIVED: updateTheme: " + themeId);
        logger.log(Priority.INFO, "API CALL: updateTheme: " + themeId);
        try {
            Theme foundTheme = themeService.getThemeById(themeId);
            foundTheme.setDescription(theme.getDescription());
            foundTheme.setName(theme.getName());
            Theme updatedTheme = themeService.editTheme(foundTheme);
            logger.log(Priority.INFO, "Updated Theme for id: " + themeId);
            return ResponseEntity.ok().body(DtoConverter.toThemeDto(updatedTheme, false));
        } catch (ThemeRepositoryException e) {
            logger.log(Priority.ERROR, "No theme found for Id: " + themeId);
            return ResponseEntity.notFound().build();
        }


    }

    @RequestMapping(value = "api/public/subtheme/{subThemeId}", method = RequestMethod.PUT)
    public ResponseEntity<SubThemeDto> updateSubTheme(@PathVariable long subThemeId, @Valid @RequestBody SubThemeDto dto) {
        System.out.println("CALL RECEIVED: updateSubTheme: " + subThemeId);
        logger.log(Priority.INFO, "API CALL: UpdateSubTheme: " + subThemeId);
        SubTheme foundSubTheme = themeService.getSubThemeById(subThemeId);
        if (foundSubTheme == null) {
            logger.log(Priority.WARN, "No Theme found for Id: " + subThemeId);
            return ResponseEntity.notFound().build();
        }
        foundSubTheme.setSubThemeName(dto.getSubThemeName());
        foundSubTheme.setSubThemeDescription(dto.getSubThemeDescription());
        foundSubTheme.setTheme(DtoConverter.toTheme(dto.getTheme(), false));
        SubTheme updatedTheme = themeService.editSubtheme(foundSubTheme);
        if (updatedTheme == null) {
            logger.log(Priority.ERROR, "Updated Theme is NULL");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok().body(DtoConverter.toSubThemeDto(updatedTheme, false));
    }

    //PUT-METHODS
    //DELET-METHODS
    @RequestMapping(value = "api/public/theme/{themeId}", method = RequestMethod.DELETE)
    public ResponseEntity<ThemeDto> deleteThemeByThemeId(@PathVariable Long themeId) {
        System.out.println("CALLED deleteThemeByThemeId: " + themeId);
        Theme deletedTheme = null;
        try {
            deletedTheme = themeService.removeThemeById(themeId);
            return ResponseEntity.ok().body(DtoConverter.toThemeDto(deletedTheme, false));
        } catch (ThemeRepositoryException e) {
            return ResponseEntity.notFound().build();
        }


    }

    @RequestMapping(value = "api/public/theme", method = RequestMethod.DELETE)
    public ResponseEntity<ThemeDto> deleteThemeByName(@RequestParam(value = "name") String name) {
        Theme foundTheme = themeService.getThemeByName(name);
        if (foundTheme == null) {
            return ResponseEntity.notFound().build();
        }
        themeService.removeThemeById(foundTheme.getThemeId());
        return ResponseEntity.ok().body(DtoConverter.toThemeDto(foundTheme, false));
    }

    @RequestMapping(value = "api/public/themes", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteThemes() {
        themeService.removeAllThemes();
        return ResponseEntity.ok().body("All good!");
    }

    @RequestMapping(value = "api/public/subthemes/{themeId}", method = RequestMethod.DELETE)
    public ResponseEntity<List<SubThemeDto>> deleteSubThemesByThemeId(@PathVariable(name = "themeId") long themeId) {
        List<SubTheme> deletedSubThemes = null;
        try {
            deletedSubThemes = themeService.removeSubThemesByThemeId(themeId);
        } catch (ThemeRepositoryException tse) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(deletedSubThemes.stream().map(st -> DtoConverter.toSubThemeDto(st, false)).collect(Collectors.toList()));
    }
    //DELETE-METHODS

    /**
     * Temporary Testing Method to ensure clean database data.
     *
     * @return
     */

    @RequestMapping(value = "api/public/cards", method = RequestMethod.POST)
    public ResponseEntity<CardDto> createCard(@Valid @RequestBody CardDto card) {
        try {
            Card cardAdded = themeService.addCard(DtoConverter.toCard(card, false));
            return ResponseEntity.ok().body(DtoConverter.toCardDto(cardAdded, false));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @RequestMapping(value = "/api/public/cards", method = RequestMethod.GET)
    public ResponseEntity<List<CardDto>> getAllCards() {
        try {
            List<CardDto> cards = themeService.getAllCards().stream().map(c -> DtoConverter.toCardDto(c, false)).collect(Collectors.toList());
            return ResponseEntity.ok().body(cards);
        } catch (ThemeRepositoryException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @RequestMapping(value = "/api/public/card/{cardId}", method = RequestMethod.GET)
    public ResponseEntity<Card> getCardById(@PathVariable long cardId) {
        try {
            Card card = themeService.getCardById(cardId);
            return ResponseEntity.ok(card);
        } catch (ThemeRepositoryException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping(value = "/api/public/subtheme/{subThemeId}/cards/{cardId}", method = RequestMethod.POST)
    public ResponseEntity<SubTheme> addCardToSubTheme(@PathVariable(name = "subThemeId") long subThemeId, @PathVariable(name = "cardId") long cardId) {
        try {
            SubTheme updatedSubTheme = themeService.addCardToSubTheme(cardId, subThemeId);
            SubTheme getSubtheme = themeService.getSubThemeById(subThemeId);
            return ResponseEntity.ok().body(updatedSubTheme);
        } catch (ThemeRepositoryException te) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @RequestMapping(value = "/api/public/cards/", method = RequestMethod.PUT)
    public ResponseEntity<Card> updateCard(@Valid @RequestBody Card card) {
        try {
            Card updatedCard = themeService.editCard(card);
            return ResponseEntity.ok(updatedCard);
        } catch (ThemeRepositoryException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping(value = "/api/public/card/{cardId}", method = RequestMethod.DELETE)
    public ResponseEntity<Card> deleteCardById(@PathVariable long cardId) {
        try {
            Card card = themeService.removeCardById(cardId);
            return ResponseEntity.ok(card);
        } catch (ThemeRepositoryException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }
}
