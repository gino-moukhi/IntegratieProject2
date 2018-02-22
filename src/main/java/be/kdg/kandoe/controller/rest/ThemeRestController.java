package be.kdg.kandoe.controller.rest;

import be.kdg.kandoe.domain.theme.Theme;
import be.kdg.kandoe.dto.DtoConverter;
import be.kdg.kandoe.dto.ThemeDto;
import be.kdg.kandoe.repository.implementation.ThemeRepositoryImpl;
import be.kdg.kandoe.service.declaration.ThemeService;
import be.kdg.kandoe.service.implementation.ThemeServiceImpl;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ThemeRestController {
    private final Logger logger = Logger.getLogger(ThemeRestController.class);

    private ThemeService themeService;

    public ThemeRestController(ThemeService themeService){
        this.themeService=themeService;
    }
    @RequestMapping(value = "api/themes", method = RequestMethod.GET)
    public ResponseEntity<List<ThemeDto>> getAllThemes(){
        System.out.println("CALL RECEIVED: getAllThemes");
        List<Theme> allThemes =themeService.getAllThemes();
        return ResponseEntity.ok().body(allThemes.stream().map(t-> DtoConverter.toThemeDto(t)).collect(Collectors.toList()));
    }

    @RequestMapping(value= "api/theme/{themeId}", method = RequestMethod.GET)
    public ResponseEntity<ThemeDto> getThemeById(@PathVariable Long themeId){
        System.out.println("CALLED RECEIVED: getThemeById: "+themeId);
        Theme theme = themeService.getThemeById(themeId);
        if(theme !=null){
            return ResponseEntity.ok().body(DtoConverter.toThemeDto(theme));
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping(value= "api/theme", method = RequestMethod.GET)
    public ResponseEntity<ThemeDto> getThemeByName(@RequestParam(name = "name") String name){
        System.out.println("CALLED RECEIVED: getThemeByName: "+name);
        Theme theme = themeService.getThemeByName(name);
        if(theme !=null){
            return ResponseEntity.ok().body(DtoConverter.toThemeDto(theme));
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping(value = "api/themes", method = RequestMethod.POST)
    public ResponseEntity<ThemeDto> CreateTheme(@Valid @RequestBody ThemeDto theme){
        System.out.println("CALL RECEIVED: CreateTheme");
        logger.log(Priority.INFO,"API CALL: CreateTheme");
        Theme createdTheme = themeService.addTheme(new Theme(theme));
        if(createdTheme==null){
            logger.log(Priority.ERROR,"ThemeService.addTheme returns NULL..");
            return ResponseEntity.badRequest().build();
        }else{
            logger.log(Priority.INFO,"Successfully created new Theme");
            return ResponseEntity.ok().body(DtoConverter.toThemeDto(createdTheme));
        }
    }

    @RequestMapping(value = "api/theme/{themeId}", method = RequestMethod.PUT)
    public ResponseEntity<ThemeDto> updateTheme(@PathVariable Long themeId, @Valid @RequestBody ThemeDto theme){
        System.out.println("CALL RECEIVED: updateTheme: "+themeId);
        logger.log(Priority.INFO,"API CALL: updateTheme: "+themeId);
        Theme foundTheme = themeService.getThemeById(themeId);
        if(foundTheme == null){
            logger.log(Priority.ERROR,"No theme found for Id: "+themeId);
            return ResponseEntity.notFound().build();
        }
        foundTheme.setDescription(theme.getDescription());
        foundTheme.setName(theme.getName());
        Theme updatedTheme=themeService.editTheme(foundTheme);
        logger.log(Priority.INFO,"Updated Theme for id: "+themeId);
        return ResponseEntity.ok().body(DtoConverter.toThemeDto(updatedTheme));
    }
    @RequestMapping(value = "api/theme/{themeId}",method = RequestMethod.DELETE)
    public ResponseEntity<ThemeDto> deleteThemeByThemeId(@PathVariable Long themeId){
        System.out.println("CALLED deleteTheByThemeId: "+themeId);
        Theme foundTheme =themeService.getThemeById(themeId);
        if(foundTheme==null){
            return ResponseEntity.notFound().build();
        }
        Theme deletedTheme=themeService.removeTheme(foundTheme);
        return ResponseEntity.ok().body(DtoConverter.toThemeDto(foundTheme));
    }

    @RequestMapping(value = "api/theme", method = RequestMethod.DELETE)
    public ResponseEntity<ThemeDto> deleteThemeByName(@RequestParam(value = "name") String name){
        Theme foundTheme = themeService.getThemeByName(name);
        if(foundTheme==null){
            return ResponseEntity.notFound().build();
        }
        themeService.removeThemeById(foundTheme.getThemeId());
        return ResponseEntity.ok().body(DtoConverter.toThemeDto(foundTheme));
    }

    /**
     * Temporary Testing Method to ensure clean database data.
     * @return
     */
    @RequestMapping(value = "api/themes", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteThemes(){
       themeService.removeAllThemes();
       return ResponseEntity.ok().body("All good!");
    }
}
