package com.veterinaria.vet.services;
import java.time.LocalDateTime;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender emailSender;
    
    public void enviarConfirmacionCita(String toEmail, String nombreCliente, 
                                     String nombreMascota, LocalDateTime fechaCita) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("agendapeludavet@gmail.com");
            message.setTo(toEmail);
            message.setSubject("Confirmación de Cita Veterinaria");
            message.setText(String.format(
                "Estimado/a %s,\n\n" +
                "Su cita ha sido programada exitosamente para su mascota %s " +
                "el día %s a las %s.\n\n" +
                "Por favor, llegue 10 minutos antes de su cita.\n\n" +
                "Recibirá un recordatorio un día antes de su cita.\n\n" +
                "Saludos cordiales,\n" +
                "Veterinaria",
                nombreCliente,
                nombreMascota,
                fechaCita.toLocalDate(),
                fechaCita.toLocalTime()
            ));
            
            emailSender.send(message);
            log.info("Email de confirmación enviado a: {}", toEmail);
        } catch (Exception e) {
            log.error("Error al enviar email de confirmación: {}", e.getMessage());
            throw new RuntimeException("Error al enviar email", e);
        }
    }
    
    public void enviarRecordatorioCita(String toEmail, String nombreCliente, 
                                     String nombreMascota, LocalDateTime fechaCita) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("agendapeludavet@gmail.com");
            message.setTo(toEmail);
            message.setSubject("Recordatorio de Cita Veterinaria");
            message.setText(String.format(
                "Estimado/a %s,\n\n" +
                "Le recordamos que tiene una cita programada para su mascota %s " +
                "mañana %s a las %s.\n\n" +
                "Por favor, llegue 10 minutos antes de su cita.\n\n" +
                "Saludos cordiales,\n" +
                "Veterinaria",
                nombreCliente,
                nombreMascota,
                fechaCita.toLocalDate(),
                fechaCita.toLocalTime()
            ));
            
            emailSender.send(message);
            log.info("Email de recordatorio enviado a: {}", toEmail);
        } catch (Exception e) {
            log.error("Error al enviar email de recordatorio: {}", e.getMessage());
            throw new RuntimeException("Error al enviar email", e);
        }
    }
}