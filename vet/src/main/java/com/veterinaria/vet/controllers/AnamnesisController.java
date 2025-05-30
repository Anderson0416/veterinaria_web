package com.veterinaria.vet.controllers;

import com.veterinaria.vet.dtos.AnamnesisCreateDto;
import com.veterinaria.vet.dtos.AnamnesisDto;
import com.veterinaria.vet.services.AnamnesisService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/anamnesis")
@RequiredArgsConstructor
@Validated
public class AnamnesisController {
    
    private final AnamnesisService anamnesisService;

    @PostMapping("/with-historial")
    public ResponseEntity<AnamnesisDto> createWithHistorial(@Valid @RequestBody AnamnesisCreateDto anamnesisCreateDto) {
        System.out.println("-----------Iniciando creación de anamnesis con historial: inicial" + anamnesisCreateDto);
        try {
            AnamnesisDto created = anamnesisService.createWithHistorial(anamnesisCreateDto);
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
