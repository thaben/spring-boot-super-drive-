package com.udacity.jwdnd.course1.cloudstorage.controllers;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.udacity.jwdnd.course1.cloudstorage.entities.Credential;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;

@Controller
@RequestMapping("/credential")
public class CredentialController {

    private CredentialService credentialService;

    public CredentialController(CredentialService credentialService) {
        this.credentialService = credentialService;
    }

    @PostMapping("/save")
    public String credentialSaveOrUpdate(
            @ModelAttribute("credential") Credential credential,
            HttpSession httpSession) {
        credential.setUserid((Integer) httpSession.getAttribute("userId"));
        Boolean isSuccess = credentialService.saveOrUpdateCredential(credential);
        return "redirect:/result?isSuccess=" + isSuccess;
    }

    @GetMapping("/delete")
    public String credentialDeletion(
            @RequestParam(required = true, name = "credentialid") Integer credentialId,
            HttpSession httpSession) {

        Boolean isSuccess = credentialService.deleteCredential(credentialId, (Integer) httpSession.getAttribute("userId"));
        return "redirect:/result?isSuccess=" + isSuccess;
    }
}