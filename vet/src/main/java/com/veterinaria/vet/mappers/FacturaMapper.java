package com.veterinaria.vet.mappers;

import com.veterinaria.vet.dtos.FacturaDto;
import com.veterinaria.vet.entities.Factura;
import com.veterinaria.vet.entities.Consulta;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface FacturaMapper {
    
    @Mapping(target = "consultaIds", source = "consultas", qualifiedByName = "consultasToIds")
    @Mapping(target = "clienteId", source = "cliente.id")
    FacturaDto toDto(Factura factura);

    @Mapping(target = "consultas", ignore = true)
    @Mapping(target = "cliente.id", source = "clienteId")
    Factura toEntity(FacturaDto facturaDto);

    @Named("consultasToIds")
    default List<Long> consultasToIds(List<Consulta> consultas) {
        if (consultas == null) {
            return Collections.emptyList();
        }
        return consultas.stream()
                .map(Consulta::getId)
                .collect(Collectors.toList());
    }
}