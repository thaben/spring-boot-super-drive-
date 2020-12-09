package com.udacity.jwdnd.course1.cloudstorage.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.udacity.jwdnd.course1.cloudstorage.dtos.MyUserDTO;
import com.udacity.jwdnd.course1.cloudstorage.dtos.mappers.MyUserDTOMapper;
import com.udacity.jwdnd.course1.cloudstorage.entities.MyUser;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.FilesService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;

@Controller
public class HomeController {

    private Logger logger = LoggerFactory.getLogger(HomeController.class);

    private UserService userService;

    private NoteService noteService;

    private MyUserDTOMapper myUserDTOMapper;

    private CredentialService credentialService;

    private FilesService filesService;

    public HomeController(UserService userService, MyUserDTOMapper myUserDTOMapper, NoteService noteService,
            CredentialService credentialService, FilesService filesService) {
        this.userService = userService;
        this.myUserDTOMapper = myUserDTOMapper;
        this.noteService = noteService;
        this.credentialService = credentialService;
        this.filesService = filesService;
    }

    @GetMapping("/home")
    public ModelAndView homePage(
            Authentication authentication,
            HttpSession httpSession
    ) {
        String username = (String) authentication.getPrincipal();

        MyUser myuser = userService.getUserByUsername(username);
        httpSession.setAttribute("userId", myuser.getUserid());

        Map<String, Object> modelData = new HashMap<>();

        modelData.put("notes", noteService.getNotesByUserId(myuser.getUserid()));
        modelData.put("credentials", credentialService.findAllCredentials(myuser.getUserid()));
        modelData.put("files", filesService.getAllFilesByUserId(myuser.getUserid()));
        return new ModelAndView("home", modelData);
    }

    @GetMapping("/login")
    public ModelAndView login() {
        return new ModelAndView("login");
    }

    @GetMapping("/signup")
    public String signupForm(Model model) {

        Map<String, Object> data = new HashMap<>();

        data.put("userNameTaken", false);
        data.put("isSuccess", false);

        model.addAllAttributes(data);

        return "signup";
    }

    @PostMapping("/signup")
    public String signUpSubmit(
            @ModelAttribute("user") MyUserDTO userDTO,
            Model model,
            HttpSession httpSession) {
        logger.info("Received user info from Signup Form: {} ", userDTO);

        MyUser user = myUserDTOMapper.dtoToDomain(userDTO);

        Boolean doesUsernameExist = userService.createUser(user);
        if (!doesUsernameExist) {
            logger.info("Username is already taken");
            model.addAttribute("userNameTaken", true);
            return "signup";
        } else {
            logger.info("User successfully saved");
            model.addAttribute("isSuccess", true);
        }
        return "redirect:/login";
    }

    @GetMapping("/result")
    public String resultPage(
            Authentication authentication,
            @RequestParam(required = false, name = "isSuccess") Boolean isSuccess,
            @RequestParam(required = false, name = "errorType") Integer errorType,
            Model model
    ) {
        Map<String, Object> data = new HashMap<>();

        data.put("isSuccess", isSuccess);
        data.put("errorType", errorType);

        model.addAllAttributes(data);

        return "result";
    }

}