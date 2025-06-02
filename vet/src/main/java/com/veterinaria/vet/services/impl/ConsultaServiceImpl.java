package com.veterinaria.vet.services.impl;

import com.veterinaria.vet.dtos.ConsultaDto;
import com.veterinaria.vet.entities.Consulta;
import com.veterinaria.vet.mappers.ConsultaMapper;
import com.veterinaria.vet.repositories.ConsultaRepository;
import com.veterinaria.vet.services.ConsultaService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConsultaServiceImpl implements ConsultaService {

    private final ConsultaRepository consultaRepository;
    private final ConsultaMapper consultaMapper;

    @Override
    @Transactional
    public ConsultaDto create(ConsultaDto consultaDto) {
        Consulta consulta = consultaMapper.toEntity(consultaDto);
        consulta = consultaRepository.save(consulta);
        return consultaMapper.toDto(consulta);
    }

    @Override
    @Transactional
    public ConsultaDto update(Long id, ConsultaDto consultaDto) {
        if (!consultaRepository.existsById(id)) {
            throw new EntityNotFoundException("Consulta no encontrada con id: " + id);
        }
        Consulta consulta = consultaMapper.toEntity(consultaDto);
        consulta.setId(id);
        consulta = consultaRepository.save(consulta);
        return consultaMapper.toDto(consulta);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!consultaRepository.existsById(id)) {
            throw new EntityNotFoundException("Consulta no encontrada con id: " + id);
        }
        consultaRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public ConsultaDto findById(Long id) {
        return consultaRepository.findById(id)
                .map(consultaMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Consulta no encontrada con id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConsultaDto> findAll() {
        return consultaRepository.findAll().stream()
                .map(consultaMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConsultaDto> findByFacturaId(Long facturaId) {
        return consultaRepository.findByFacturaId(facturaId).stream()
                .map(consultaMapper::toDto)
                .collect(Collectors.toList());
    }
}