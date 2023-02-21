package com.example.registration.controller;

import com.example.registration.exception.UserAlreadyExistsException;
import com.example.registration.model.UserDto;
import com.example.registration.repository.UserRepository;
import com.example.registration.service.securitySession.SessionUserServiceImpl;
import com.example.registration.service.UserServiceImpl;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Log4j2
public class UserController {

    @Autowired
    private UserRepository userRepository;
    private SessionUserServiceImpl sessionUserService;

    private UserServiceImpl userService;

    public UserController(UserServiceImpl userService, SessionUserServiceImpl sessionUserService) {
        this.userService = userService;
        this.sessionUserService = sessionUserService;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String ViewHomePage() {

        return "index";
    }

    @GetMapping("/login")
    public String login() {

        return "login";
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserDto());

        return "signup_form";
    }

    @PostMapping("/process_register")
    @ResponseStatus(HttpStatus.CREATED)
    public String processRegister(UserDto userDto, BindingResult bindingResult) throws UserAlreadyExistsException {
        userService.createUser(userDto);

        return "register_success.html";
    }

    @GetMapping("/users")
    public String listUsers(Model model) {
        List<UserDto> listUsers = userService.findAllUsers();
        model.addAttribute("listUsers", listUsers);

        sessionUserService.getUserLoggedInfo();

        return "users";
    }
}
