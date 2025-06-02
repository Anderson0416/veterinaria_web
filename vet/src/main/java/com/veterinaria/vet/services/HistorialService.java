package com.veterinaria.vet.services;
import java.util.List;

import com.veterinaria.vet.dtos.HistorialResponseDto;

public interface HistorialService {
    HistorialResponseDto findById(Long id);
    List<HistorialResponseDto> findAll();
    List<HistorialResponseDto> findByMascotaId(Long mascotaId);
}