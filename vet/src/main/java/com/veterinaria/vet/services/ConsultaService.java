package com.veterinaria.vet.services;

import com.veterinaria.vet.dtos.ConsultaDto;
import java.util.List;

public interface ConsultaService {
    ConsultaDto create(ConsultaDto consultaDto);
    ConsultaDto update(Long id, ConsultaDto consultaDto);
    void delete(Long id);
    ConsultaDto findById(Long id);
    List<ConsultaDto> findAll();
    List<ConsultaDto> findByFacturaId(Long facturaId);
}