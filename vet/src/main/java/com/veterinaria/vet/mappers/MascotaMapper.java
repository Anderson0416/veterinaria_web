package com.veterinaria.vet.mappers;

import com.veterinaria.vet.dtos.MascotaDto;
import com.veterinaria.vet.entities.Mascota;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MascotaMapper {
    @Mapping(target = "clienteId", source = "cliente.id")
    MascotaDto toDto(Mascota mascota);

    @Mapping(target = "cliente.id", source = "clienteId")
    Mascota toEntity(MascotaDto mascotaDto);
}