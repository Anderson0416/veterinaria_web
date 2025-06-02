package com.veterinaria.vet.services.impl;

import com.veterinaria.vet.dtos.ClienteDto;
import com.veterinaria.vet.entities.Cliente;
import com.veterinaria.vet.exceptions.AppException;
import com.veterinaria.vet.mappers.ClienteMapper;
import com.veterinaria.vet.repositories.ClienteRepository;
import com.veterinaria.vet.services.ClienteService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;

    @Override
    @Transactional
    public ClienteDto create(ClienteDto clienteDto) {
        if (clienteRepository.existsByEmail(clienteDto.getEmail())) {
            throw new AppException("El email ya está registrado", HttpStatus.BAD_REQUEST);
        }
        Cliente cliente = clienteMapper.toEntity(clienteDto);
        cliente = clienteRepository.save(cliente);
        return clienteMapper.toDto(cliente);
    }

    @Override
    @Transactional
    public ClienteDto update(Long id, ClienteDto clienteDto) {
        if (!clienteRepository.existsById(id)) {
            throw new EntityNotFoundException("Cliente no encontrado con id: " + id);
        }
        Cliente cliente = clienteMapper.toEntity(clienteDto);
        cliente.setId(id);
        cliente = clienteRepository.save(cliente);
        return clienteMapper.toDto(cliente);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new EntityNotFoundException("Cliente no encontrado con id: " + id);
        }
        clienteRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public ClienteDto findById(Long id) {
        return clienteRepository.findById(id)
                .map(clienteMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado con id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClienteDto> findAll() {
        return clienteRepository.findAll().stream()
                .map(clienteMapper::toDto)
                .collect(Collectors.toList());
    }
}