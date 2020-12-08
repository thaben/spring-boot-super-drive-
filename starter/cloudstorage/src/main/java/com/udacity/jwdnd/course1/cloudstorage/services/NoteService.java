package com.udacity.jwdnd.course1.cloudstorage.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.udacity.jwdnd.course1.cloudstorage.entities.Note;
import com.udacity.jwdnd.course1.cloudstorage.repository.NoteRepository;

@Service
public class NoteService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NoteService.class);

    NoteRepository noteRepository;

    NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public Boolean createNewNote(Note note) {
        try {
            noteRepository.save(note);
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    public Boolean createOrUpdateNote(Note note) {
        if (note.getNoteid() != null) {
            return updateNote(note);
        } else {
            return saveNote(note);
        }
    }

    public Boolean saveNote(Note note) {
        try {
            LOGGER.info("Saving new note");
            return noteRepository.save(note) > 0;
        } catch (Exception e) {
            LOGGER.error("Error while saving new note");
            e.printStackTrace();
            return false;
        }

    }

    public Boolean updateNote(Note note) {
        try {
            LOGGER.info("Updating new note");
            return noteRepository.update(note) > 0;
        } catch (Exception e) {
            LOGGER.error("Error while Updating new note");
            e.printStackTrace();
            return false;
        }

    }

    public List<Note> getNotesByUserId(Integer userId) {
        return noteRepository.findAllByUserId(userId);
    }

    public Boolean deleteNote(Integer noteId, Integer userId) {
        try {
            return noteRepository.delete(noteId, userId) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }
}