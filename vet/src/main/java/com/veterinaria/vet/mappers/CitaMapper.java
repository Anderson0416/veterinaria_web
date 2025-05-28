package com.veterinaria.vet.mappers;

import com.veterinaria.vet.dtos.CitaDto;
import com.veterinaria.vet.dtos.CitaResponseDto;
import com.veterinaria.vet.entities.Cita;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ClienteMapper.class, MascotaMapper.class, UserMapper.class, FacturaMapper.class})
public interface CitaMapper {
    @Mapping(target = "clienteId", source = "cliente.id")
    @Mapping(target = "mascotaId", source = "mascota.id")
    @Mapping(target = "veterinarioId", source = "veterinario.id")
    @Mapping(target = "facturaId", source = "factura.id")
    CitaDto toDto(Cita cita);

    CitaResponseDto toResponseDto(Cita cita);

    @Mapping(target = "cliente.id", source = "clienteId")
    @Mapping(target = "mascota.id", source = "mascotaId")
    @Mapping(target = "veterinario.id", source = "veterinarioId")
    @Mapping(target = "factura.id", source = "facturaId")
    Cita toEntity(CitaDto citaDto);
}