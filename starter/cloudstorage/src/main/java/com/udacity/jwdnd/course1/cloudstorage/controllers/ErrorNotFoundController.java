package com.udacity.jwdnd.course1.cloudstorage.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorNotFoundController implements ErrorController {

    @Override
    @RequestMapping("/error")
    public String getErrorPath() {
        return "error404";
    }
}
