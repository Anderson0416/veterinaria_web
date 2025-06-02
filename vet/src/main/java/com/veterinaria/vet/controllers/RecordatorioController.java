package com.veterinaria.vet.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import lombok.RequiredArgsConstructor;
import com.veterinaria.vet.services.RecordatorioService;

@RestController
@RequestMapping("/recordatorios")
@RequiredArgsConstructor
public class RecordatorioController {
    
    private final RecordatorioService recordatorioService;
    
    @PostMapping("/enviar")
    public ResponseEntity<String> enviarRecordatoriosManualmente() {
        try {
            recordatorioService.enviarRecordatorios();
            return ResponseEntity.ok("Recordatorios enviados correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                   .body("Error al enviar recordatorios: " + e.getMessage());
        }
    }
}