package com.agacorporation.demo.component;

import com.agacorporation.demo.component.commands.UserFilter;
import com.agacorporation.demo.domain.Room;
import com.agacorporation.demo.domain.User;
import com.agacorporation.demo.service.EmailService;
import com.agacorporation.demo.service.SecurityService;
import com.agacorporation.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Locale;
import java.util.Optional;

@Controller
@RequestMapping
@SessionAttributes("userForm")
public class UserController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    private final UserService userService;
    private final SecurityService securityService;

    public UserController(UserService userService, SecurityService securityService) {
        this.userService = userService;
        this.securityService = securityService;
    }

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("userForm", new User());
        return "registration.html";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/addUser")
    public String addUser(Model model) {
        model.addAttribute("userForm", new User());
        return "addUser.html";
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value="/editUser.html", method= RequestMethod.GET)
    public String showFormUSR(Model model, Optional<Long> id){
        model.addAttribute("userForm",
                id.isPresent()?
                        userService.getUser(id.get()):
                        new User());
        System.out.println("login get: "+userService.getUser(id.get()).getLogin());
        return "editUser.html";
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/editUser.html")
    public String editUser(@ModelAttribute("userForm") User userForm){
        System.out.println("login post: "+userForm.getLogin());
        userService.save(userForm);
        emailService.send(userForm.getEmail(), "Data Change",
                "Your account details have been changed.");
        return "redirect:userList.html";
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/addUser")
    public String addUser(@ModelAttribute("userForm") User userForm, BindingResult bindingResult){
        userForm.setPassword(passwordEncoder.encode("temporary"));
        userService.save(userForm);
        emailService.send(userForm.getEmail(), "Welcome to Woods Hotel!",
                "Your account registration was successful. Your login is: " + userForm.getLogin() + ". The password has been automatically generated. Please change your password at: www.woods.com");
        return "welcome.html";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute("userForm") User userForm, BindingResult bindingResult) {
        userService.save(userForm);
        emailService.send(userForm.getEmail(), "Welcome to Woods Hotel!",
                "Your account registration was successful. Your login is: " + userForm.getLogin() + ". We invite you to: www.woods.com");

        return "welcome.html";
    }

    @PostMapping("/login")
    public String login(Model model, String error, String logout) {
        if (error != null)
            model.addAttribute("error", "Your username and password are invalid.");

        if (logout != null)
            model.addAttribute("message", "You have been logged out successfully.");
        return "welcome.html";
    }

    @GetMapping("/login")
    public String login() {
        return "login.html";
    }

    @GetMapping({"/", "/welcome"})
    public String welcome(Model model) {
        return "welcome.html";}}
