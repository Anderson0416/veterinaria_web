package com.veterinaria.vet.mappers;

import com.veterinaria.vet.dtos.HistorialDto;
import com.veterinaria.vet.dtos.HistorialResponseDto;
import com.veterinaria.vet.entities.Historial;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {AnamnesisMapper.class, MascotaMapper.class})
public interface HistorialMapper {
    @Mapping(target = "mascota", source = "mascota")
    @Mapping(target = "anamnesis", source = "anamnesis")
    HistorialResponseDto toResponseDto(Historial historial);

    @Mapping(target = "mascotaId", source = "mascota.id")
    @Mapping(target = "anamnesisId", source = "anamnesis.id")
    HistorialDto toDto(Historial historial);

    Historial toEntity(HistorialDto historialDto);
}