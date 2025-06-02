package com.veterinaria.vet.services.impl;

import com.veterinaria.vet.dtos.FacturaDto;
import com.veterinaria.vet.entities.Consulta;
import com.veterinaria.vet.entities.Factura;
import com.veterinaria.vet.entities.Cliente;
import com.veterinaria.vet.mappers.FacturaMapper;
import com.veterinaria.vet.repositories.ConsultaRepository;
import com.veterinaria.vet.repositories.FacturaRepository;
import com.veterinaria.vet.repositories.ClienteRepository;
import com.veterinaria.vet.services.FacturaService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FacturaServiceImpl implements FacturaService {
    private final FacturaRepository facturaRepository;
    private final ConsultaRepository consultaRepository;
    private final ClienteRepository clienteRepository;
    private final FacturaMapper facturaMapper;

    @Override
    @Transactional
    public FacturaDto create(FacturaDto facturaDto) {
        // Validar y cargar el cliente
        Cliente cliente = clienteRepository.findById(facturaDto.getClienteId())
            .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado con id: " + facturaDto.getClienteId()));

        Factura factura = facturaMapper.toEntity(facturaDto);
        factura.setCliente(cliente);
        
        // Cargar las consultas
        if (facturaDto.getConsultaIds() != null && !facturaDto.getConsultaIds().isEmpty()) {
            List<Consulta> consultas = consultaRepository.findAllById(facturaDto.getConsultaIds());
            factura.setConsultas(consultas);
        }

        factura = facturaRepository.save(factura);
        return facturaMapper.toDto(factura);
    }

    @Override
    @Transactional
    public FacturaDto update(Long id, FacturaDto facturaDto) {
        if (!facturaRepository.existsById(id)) {
            throw new EntityNotFoundException("Factura no encontrada con id: " + id);
        }
        Factura factura = facturaMapper.toEntity(facturaDto);
        factura.setId(id);
        factura = facturaRepository.save(factura);
        return facturaMapper.toDto(factura);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!facturaRepository.existsById(id)) {
            throw new EntityNotFoundException("Factura no encontrada con id: " + id);
        }
        facturaRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public FacturaDto findById(Long id) {
        return facturaRepository.findById(id)
                .map(facturaMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Factura no encontrada con id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<FacturaDto> findAll() {
        return facturaRepository.findAll().stream()
                .map(facturaMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<FacturaDto> findByClienteId(Long clienteId) {
        return facturaRepository.findByClienteId(clienteId).stream()
                .map(facturaMapper::toDto)
                .collect(Collectors.toList());
    }
}