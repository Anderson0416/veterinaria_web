package com.veterinaria.vet.services.impl;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.veterinaria.vet.dtos.HistorialResponseDto;
import com.veterinaria.vet.mappers.HistorialMapper;
import com.veterinaria.vet.repositories.HistorialRepository;
import com.veterinaria.vet.services.HistorialService;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HistorialServiceImpl implements HistorialService {
    private final HistorialRepository historialRepository;
    private final HistorialMapper historialMapper;

    @Override
    @Transactional(readOnly = true)
    public HistorialResponseDto findById(Long id) {
        return historialRepository.findById(id)
                .map(historialMapper::toResponseDto)
                .orElseThrow(() -> new EntityNotFoundException("Historial no encontrado"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<HistorialResponseDto> findAll() {
        return historialRepository.findAll().stream()
                .map(historialMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<HistorialResponseDto> findByMascotaId(Long mascotaId) {
        return historialRepository.findByMascotaId(mascotaId).stream()
                .map(historialMapper::toResponseDto)
                .collect(Collectors.toList());
    }
}