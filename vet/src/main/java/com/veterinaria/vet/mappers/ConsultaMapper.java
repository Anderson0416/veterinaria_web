package com.veterinaria.vet.mappers;

import com.veterinaria.vet.dtos.ConsultaDto;
import com.veterinaria.vet.entities.Consulta;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ConsultaMapper {
    ConsultaMapper INSTANCE = Mappers.getMapper(ConsultaMapper.class);

    ConsultaDto toDto(Consulta consulta);
    Consulta toEntity(ConsultaDto consultaDto);
}