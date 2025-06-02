package com.veterinaria.vet.services.impl;

import com.veterinaria.vet.dtos.CitaCreateDto;
import com.veterinaria.vet.dtos.CitaDto;
import com.veterinaria.vet.dtos.CitaResponseDto;
import com.veterinaria.vet.dtos.FacturaDto;
import com.veterinaria.vet.entities.Cita;
import com.veterinaria.vet.entities.Cliente;
import com.veterinaria.vet.entities.Mascota;
import com.veterinaria.vet.exceptions.AppException;
import com.veterinaria.vet.mappers.CitaMapper;
import com.veterinaria.vet.repositories.CitaRepository;
import com.veterinaria.vet.services.CitaService;
import com.veterinaria.vet.services.FacturaService;
import com.veterinaria.vet.services.EmailService;
import com.veterinaria.vet.repositories.ClienteRepository;
import com.veterinaria.vet.repositories.MascotaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CitaServiceImpl implements CitaService {
    private final CitaRepository citaRepository;
    private final CitaMapper citaMapper;
    private final FacturaService facturaService;
    private final EmailService emailService;
    private final ClienteRepository clienteRepository; // Añadir esta dependencia
    private final MascotaRepository mascotaRepository; // Añadir esta dependencia

    @Override
    @Transactional
    public CitaDto create(CitaDto citaDto) {
        Cita cita = citaMapper.toEntity(citaDto);
        cita = citaRepository.save(cita);
        
        // Buscar los datos necesarios para el email
        try {
            Cliente cliente = clienteRepository.findById(citaDto.getClienteId())
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado"));
            
            Mascota mascota = mascotaRepository.findById(citaDto.getMascotaId())
                .orElseThrow(() -> new EntityNotFoundException("Mascota no encontrada"));
            
            emailService.enviarConfirmacionCita(
                cliente.getEmail(),
                cliente.getNombre(),
                mascota.getNombre(),
                cita.getFecha()
            );
        } catch (Exception e) {
            log.error("Error al enviar email de confirmación: {}", e.getMessage());
            // No lanzamos la excepción para que la cita se guarde de todos modos
        }
        
        return citaMapper.toDto(cita);
    }

    @Override
    @Transactional
    public CitaDto update(Long id, CitaDto citaDto) {
        if (!citaRepository.existsById(id)) {
            throw new EntityNotFoundException("Cita no encontrada con id: " + id);
        }
        Cita cita = citaMapper.toEntity(citaDto);
        cita.setId(id);
        cita = citaRepository.save(cita);
        return citaMapper.toDto(cita);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!citaRepository.existsById(id)) {
            throw new EntityNotFoundException("Cita no encontrada con id: " + id);
        }
        citaRepository.deleteById(id);
    }
    @Override
    @Transactional
    public void deleteByVeterinarioId(Long veterinarioId) {
        List<Cita> citas = citaRepository.findByVeterinarioId(veterinarioId);
        if (citas.isEmpty()) {
            throw new EntityNotFoundException("No se encontraron citas para el veterinario con id: " + veterinarioId);
        }
        System.out.println("Eliminando citas para el veterinario con id: " + veterinarioId);
        citaRepository.deleteAll(citas);
    }
    @Override
    @Transactional(readOnly = true)
    public CitaResponseDto findById(Long id) {
        return citaRepository.findById(id)
                .map(citaMapper::toResponseDto)
                .orElseThrow(() -> new EntityNotFoundException("Cita no encontrada con id: " + id));
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<CitaResponseDto> findAll() {
        return citaRepository.findAll().stream()
                .map(citaMapper::toResponseDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<CitaResponseDto> findByClienteId(Long clienteId) {
        return citaRepository.findByClienteId(clienteId).stream()
                .map(citaMapper::toResponseDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<CitaResponseDto> findByVeterinarioId(Long veterinarioId) {
        return citaRepository.findByVeterinarioId(veterinarioId).stream()
                .map(citaMapper::toResponseDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<CitaResponseDto> findByMascotaId(Long mascotaId) {
        return citaRepository.findByMascotaId(mascotaId).stream()
                .map(citaMapper::toResponseDto)
                .collect(Collectors.toList());
    }
    @Override
    @Transactional
    public CitaDto createWithFactura(CitaCreateDto citaCreateDto) {
        // Validar que existan consultas
        if (citaCreateDto.getConsultaIds() == null || citaCreateDto.getConsultaIds().isEmpty()) {
            throw new AppException("Debe incluir al menos una consulta", HttpStatus.BAD_REQUEST);
        }

        FacturaDto facturaDto = FacturaDto.builder()
                .fecha(LocalDateTime.now())
                .total(citaCreateDto.getTotal())
                .clienteId(citaCreateDto.getClienteId())
                .consultaIds(citaCreateDto.getConsultaIds())
                .fecha(LocalDateTime.now())
                .build();
        
        FacturaDto facturaCreada = facturaService.create(facturaDto);
        // Luego creamos la cita con el ID de la factura
        CitaDto citaDto = CitaDto.builder()
                .fecha(citaCreateDto.getFecha())
                .anotaciones(citaCreateDto.getAnotaciones())
                .estado("Pendiente")
                .clienteId(citaCreateDto.getClienteId())
                .mascotaId(citaCreateDto.getMascotaId())
                .veterinarioId(citaCreateDto.getVeterinarioId())
                .facturaId(facturaCreada.getId()) // Aquí usamos el ID de la factura creada
                .build();
        CitaDto citaCreada = create(citaDto);
        
        try {
            Cliente cliente = clienteRepository.findById(citaCreateDto.getClienteId())
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado"));
                
            Mascota mascota = mascotaRepository.findById(citaCreateDto.getMascotaId())
                .orElseThrow(() -> new EntityNotFoundException("Mascota no encontrada"));
                
            emailService.enviarConfirmacionCita(
                cliente.getEmail(),
                cliente.getNombre(),
                mascota.getNombre(),
                citaCreada.getFecha()
            );
        } catch (Exception e) {
            log.error("Error al enviar email de confirmación: {}", e.getMessage());
        }
        
        return citaCreada;
    }

    @Override
    @Transactional
    public CitaDto pagarCita(Long id) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cita no encontrada con id: " + id));
                
        cita.setEstado("Pagado");
        cita = citaRepository.save(cita);
        
        return citaMapper.toDto(cita);
    }
        @Override
    @Transactional
    public CitaDto marcarComoAtendida(Long id) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cita no encontrada con id: " + id));
                
        cita.setAtendido(true);
        cita = citaRepository.save(cita);
        
        return citaMapper.toDto(cita);
    }
}