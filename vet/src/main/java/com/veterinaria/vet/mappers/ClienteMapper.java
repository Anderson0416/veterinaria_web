package com.veterinaria.vet.mappers;

import com.veterinaria.vet.dtos.ClienteDto;
import com.veterinaria.vet.entities.Cliente;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {MascotaMapper.class})
public interface ClienteMapper {
    ClienteDto toDto(Cliente cliente);
    Cliente toEntity(ClienteDto clienteDto);
}