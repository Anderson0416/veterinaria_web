package com.veterinaria.vet.services;
import java.time.LocalDateTime;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import com.veterinaria.vet.entities.Cita;
import com.veterinaria.vet.repositories.CitaRepository;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;

@Service
@Slf4j
@RequiredArgsConstructor
public class RecordatorioService {
    
    private final CitaRepository citaRepository;
    private final EmailService emailService;
    
    @Scheduled(cron = "0 0 20 * * *") // Ejecutar todos los días a las 8 PM
    public void enviarRecordatorios() {
        LocalDateTime manana = LocalDateTime.now().plusDays(1).withHour(0).withMinute(0);
        LocalDateTime finManana = manana.plusDays(1);
        
        List<Cita> citasManana = citaRepository.findByFechaBetween(manana, finManana);
        
        for (Cita cita : citasManana) {
            try {
                emailService.enviarRecordatorioCita(
                    cita.getCliente().getEmail(),
                    cita.getCliente().getNombre(),
                    cita.getMascota().getNombre(),
                    cita.getFecha()
                );
            } catch (Exception e) {
                log.error("Error enviando recordatorio para cita ID {}: {}", 
                         cita.getId(), e.getMessage());
            }
        }
    }
}