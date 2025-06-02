package com.veterinaria.vet.services.impl;

import com.veterinaria.vet.dtos.MascotaDto;
import com.veterinaria.vet.entities.Mascota;
import com.veterinaria.vet.exceptions.AppException;
import com.veterinaria.vet.mappers.MascotaMapper;
import com.veterinaria.vet.repositories.ClienteRepository;
import com.veterinaria.vet.repositories.MascotaRepository;
import com.veterinaria.vet.services.MascotaService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MascotaServiceImpl implements MascotaService {

    private final MascotaRepository mascotaRepository;
    private final ClienteRepository clienteRepository;
    private final MascotaMapper mascotaMapper;

    @Override
    @Transactional
    public MascotaDto create(MascotaDto mascotaDto) {
        System.out.println("Datos recibidos en el servicio: " + mascotaDto);
        if (!clienteRepository.existsById(mascotaDto.getClienteId())) {
            throw new AppException("Cliente no encontrado", HttpStatus.BAD_REQUEST);
        }else{
            System.out.println("Cliente encontrado"+ mascotaDto.getClienteId());
        }
        Mascota mascota = mascotaMapper.toEntity(mascotaDto);
        mascota = mascotaRepository.save(mascota);
        return mascotaMapper.toDto(mascota);
    }

    @Override
    @Transactional
    public MascotaDto update(Long id, MascotaDto mascotaDto) {
        if (!mascotaRepository.existsById(id)) {
            throw new EntityNotFoundException("Mascota no encontrada con id: " + id);
        }
        if (!clienteRepository.existsById(mascotaDto.getClienteId())) {
            throw new AppException("Cliente no encontrado", HttpStatus.BAD_REQUEST);
        }
        Mascota mascota = mascotaMapper.toEntity(mascotaDto);
        mascota.setId(id);
        mascota = mascotaRepository.save(mascota);
        return mascotaMapper.toDto(mascota);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!mascotaRepository.existsById(id)) {
            throw new EntityNotFoundException("Mascota no encontrada con id: " + id);
        }
        mascotaRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public MascotaDto findById(Long id) {
        return mascotaRepository.findById(id)
                .map(mascotaMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Mascota no encontrada con id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MascotaDto> findAll() {
        return mascotaRepository.findAll().stream()
                .map(mascotaMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MascotaDto> findByClienteId(Long clienteId) {
        return mascotaRepository.findByClienteId(clienteId).stream()
                .map(mascotaMapper::toDto)
                .collect(Collectors.toList());
    }
}