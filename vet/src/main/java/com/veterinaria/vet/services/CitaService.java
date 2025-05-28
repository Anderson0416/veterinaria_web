package com.veterinaria.vet.services;

import com.veterinaria.vet.dtos.CitaCreateDto;
import com.veterinaria.vet.dtos.CitaDto;
import com.veterinaria.vet.dtos.CitaResponseDto;

import java.util.List;

public interface CitaService {
    CitaDto create(CitaDto citaDto);
    CitaDto createWithFactura(CitaCreateDto citaCreateDto);
    CitaDto update(Long id, CitaDto citaDto);
    void deleteByVeterinarioId(Long veterinarioId);
    void delete(Long id);
    CitaResponseDto findById(Long id);
    List<CitaResponseDto> findAll();
    List<CitaResponseDto> findByClienteId(Long clienteId);
    List<CitaResponseDto> findByVeterinarioId(Long veterinarioId);
    List<CitaResponseDto> findByMascotaId(Long mascotaId);
    CitaDto pagarCita(Long id);
    CitaDto marcarComoAtendida(Long id);

}