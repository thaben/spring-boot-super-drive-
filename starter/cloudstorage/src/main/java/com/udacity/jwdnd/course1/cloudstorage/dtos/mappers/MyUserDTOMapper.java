package com.udacity.jwdnd.course1.cloudstorage.dtos.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.udacity.jwdnd.course1.cloudstorage.dtos.MyUserDTO;
import com.udacity.jwdnd.course1.cloudstorage.entities.MyUser;

@Mapper(componentModel = "spring")
public interface MyUserDTOMapper {

    @Mapping(source = "firstName", target = "firstname")
    @Mapping(source = "lastName", target = "lastname")
    MyUser dtoToDomain(MyUserDTO dto);

    @Mapping(source = "firstname", target = "firstName")
    @Mapping(source = "lastname", target = "lastName")
    MyUserDTO domainToDto(MyUser domain);
}
