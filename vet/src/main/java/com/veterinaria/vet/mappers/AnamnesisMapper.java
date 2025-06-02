package com.veterinaria.vet.mappers;

import com.veterinaria.vet.dtos.AnamnesisDto;
import com.veterinaria.vet.dtos.AnamnesisResponseDto;
import com.veterinaria.vet.entities.Anamnesis;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AnamnesisMapper {
    AnamnesisDto toDto(Anamnesis anamnesis);
    AnamnesisResponseDto toResponseDto(Anamnesis anamnesis);
    Anamnesis toEntity(AnamnesisDto anamnesisDto);
}