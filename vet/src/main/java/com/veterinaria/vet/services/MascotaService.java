package com.veterinaria.vet.services;

import com.veterinaria.vet.dtos.MascotaDto;
import java.util.List;

public interface MascotaService {
    MascotaDto create(MascotaDto mascotaDto);
    MascotaDto update(Long id, MascotaDto mascotaDto);
    void delete(Long id);
    MascotaDto findById(Long id);
    List<MascotaDto> findAll();
    List<MascotaDto> findByClienteId(Long clienteId);
}