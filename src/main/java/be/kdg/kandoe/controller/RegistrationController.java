package be.kdg.kandoe.controller;

import be.kdg.kandoe.domain.User;
import be.kdg.kandoe.dto.UserDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class RegistrationController  {

    @GetMapping("/user/registration")
    public String showRegistrationForm(Model model){
        UserDto userDto = new UserDto();
        model.addAttribute("user", userDto);
        return "registrationForm";
    }
}
