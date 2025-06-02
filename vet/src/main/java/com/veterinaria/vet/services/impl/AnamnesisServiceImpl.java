package com.veterinaria.vet.services.impl;

import com.veterinaria.vet.dtos.AnamnesisCreateDto;
import com.veterinaria.vet.dtos.AnamnesisDto;
import com.veterinaria.vet.entities.Anamnesis;
import com.veterinaria.vet.entities.Historial;
import com.veterinaria.vet.entities.Mascota;
import com.veterinaria.vet.mappers.AnamnesisMapper;
import com.veterinaria.vet.repositories.AnamnesisRepository;
import com.veterinaria.vet.repositories.HistorialRepository;
import com.veterinaria.vet.repositories.MascotaRepository;
import com.veterinaria.vet.services.AnamnesisService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AnamnesisServiceImpl implements AnamnesisService {
    
    private final AnamnesisRepository anamnesisRepository;
    private final HistorialRepository historialRepository;
    private final MascotaRepository mascotaRepository;
    private final AnamnesisMapper anamnesisMapper;

    @Override
    @Transactional
    public AnamnesisDto createWithHistorial(AnamnesisCreateDto anamnesisCreateDto) {
        try {
            System.out.println("-----------Iniciando creación de anamnesis con historial " + anamnesisCreateDto);
            
            // Validar que exista la mascota
            Mascota mascota = mascotaRepository.findById(anamnesisCreateDto.getMascotaId())
                .orElseThrow(() -> new EntityNotFoundException("Mascota no encontrada con ID: " + anamnesisCreateDto.getMascotaId()));

            // Crear el anamnesis SIN relaciones bidireccionales
            Anamnesis anamnesis = Anamnesis.builder()
                .peso(anamnesisCreateDto.getPeso())
                .estadoReproductivo(anamnesisCreateDto.getEstadoReproductivo())
                .tipoVivienda(anamnesisCreateDto.getTipoVivienda())
                .actividadFisica(anamnesisCreateDto.getActividadFisica())
                .vacunasPrevias(anamnesisCreateDto.getVacunasPrevias())
                .vacunasPreviasDesparacitacion(anamnesisCreateDto.getVacunasPreviasDesparacitacion())
                .motivoConsulta(anamnesisCreateDto.getMotivoConsulta())
                .sintomasMascota(anamnesisCreateDto.getSintomasMascota())
                .observaciones(anamnesisCreateDto.getObservaciones())
                .dieta(anamnesisCreateDto.getDieta())
                .build();

            // Guardar anamnesis primero
            anamnesis = anamnesisRepository.save(anamnesis);
            System.out.println("---------------------Anamnesis guardado con ID: " + anamnesis.getId());

            // Crear el historial
            Historial historial = Historial.builder()
                .fecha(LocalDateTime.now())
                .mascota(mascota)
                .anamnesis(anamnesis)
                .build();

            // Guardar historial
            historial = historialRepository.save(historial);
            System.out.println("---------------------Historial guardado con ID: " + historial.getId());

            // IMPORTANTE: Mapear a DTO ANTES de que se establezcan relaciones bidireccionales
            // o usar un mapper que no incluya las relaciones circulares
            AnamnesisDto result = anamnesisMapper.toDto(anamnesis);
            
            System.out.println("---------------------Anamnesis convertido a DTO exitosamente");
            return result;
            
        } catch (EntityNotFoundException e) {
            System.err.println("-----------Error: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("-----------Error inesperado al crear anamnesis con historial: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error al crear anamnesis: " + e.getMessage(), e);
        }
    }
}