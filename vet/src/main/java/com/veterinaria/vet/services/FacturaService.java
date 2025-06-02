package com.veterinaria.vet.services;

import com.veterinaria.vet.dtos.FacturaDto;
import java.util.List;

public interface FacturaService {
    FacturaDto create(FacturaDto facturaDto);
    FacturaDto update(Long id, FacturaDto facturaDto);
    void delete(Long id);
    FacturaDto findById(Long id);
    List<FacturaDto> findAll();
    List<FacturaDto> findByClienteId(Long clienteId);
}