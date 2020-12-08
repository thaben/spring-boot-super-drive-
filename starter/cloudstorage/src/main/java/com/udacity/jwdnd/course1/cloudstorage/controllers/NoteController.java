package com.udacity.jwdnd.course1.cloudstorage.controllers;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.udacity.jwdnd.course1.cloudstorage.dtos.NoteDTO;
import com.udacity.jwdnd.course1.cloudstorage.dtos.mappers.NoteDTOMapper;
import com.udacity.jwdnd.course1.cloudstorage.entities.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;

@Controller
@RequestMapping("/note")
public class NoteController {

    private Logger logger = LoggerFactory.getLogger(NoteController.class);
    private NoteService noteService;
    private NoteDTOMapper noteDTOMapper;

    public NoteController(NoteService noteService, NoteDTOMapper noteDTOMapper) {
        this.noteService = noteService;
        this.noteDTOMapper = noteDTOMapper;
    }

    @PostMapping("/save")
    public String createNote(
            @ModelAttribute("note") NoteDTO noteDTO,
            HttpSession httpSession
    ) {
        //Convert DTO to Domain Object
        Note note = noteDTOMapper.dtoToDomain(noteDTO);
        //Set fk_userId from session
        note.setUserid((Integer) httpSession.getAttribute("userId"));
        Boolean isSuccess = noteService.createOrUpdateNote(note);
        return "redirect:/result?isSuccess=" + isSuccess;
    }

    @GetMapping("/delete")
    public String noteDeletion(
            @RequestParam(required = true, name = "noteId") Integer noteId,
            HttpSession httpSession
    ) {
        logger.info("Note with id {} is going to be deleted", noteId);
        Integer userId = (Integer) httpSession.getAttribute("userId");
        Boolean isSuccess = noteService.deleteNote(noteId, userId);
        return "redirect:/result?isSuccess=" + isSuccess;
    }
}