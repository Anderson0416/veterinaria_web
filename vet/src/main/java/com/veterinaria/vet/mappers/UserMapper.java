package com.veterinaria.vet.mappers;

import com.veterinaria.vet.dtos.SignUpDto;
import com.veterinaria.vet.dtos.UserDto;
import com.veterinaria.vet.entities.User;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toUserDto(User user);

    @Mapping(target = "contraseña", ignore = true)
    User signUpToUser(SignUpDto signUpDto);

}
