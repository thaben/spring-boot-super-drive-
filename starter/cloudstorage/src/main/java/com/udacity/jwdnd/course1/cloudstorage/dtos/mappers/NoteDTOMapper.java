package com.udacity.jwdnd.course1.cloudstorage.dtos.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.udacity.jwdnd.course1.cloudstorage.dtos.NoteDTO;
import com.udacity.jwdnd.course1.cloudstorage.entities.Note;

@Mapper(componentModel = "spring")
public interface NoteDTOMapper {

    @Mapping(source = "noteId", target = "noteid")
    @Mapping(source = "noteTitle", target = "notetitle")
    @Mapping(source = "noteDescription", target = "notedescription")
    Note dtoToDomain(NoteDTO dto);

}
