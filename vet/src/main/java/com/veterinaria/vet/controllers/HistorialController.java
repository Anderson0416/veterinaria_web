package com.veterinaria.vet.controllers;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.veterinaria.vet.dtos.HistorialResponseDto;
import com.veterinaria.vet.services.HistorialService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/historiales")
@RequiredArgsConstructor
public class HistorialController {
    private final HistorialService historialService;

    @GetMapping("/{id}")
    public ResponseEntity<HistorialResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(historialService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<HistorialResponseDto>> findAll() {
        return ResponseEntity.ok(historialService.findAll());
    }

    @GetMapping("/mascota/{mascotaId}")
    public ResponseEntity<List<HistorialResponseDto>> findByMascotaId(@PathVariable Long mascotaId) {
        return ResponseEntity.ok(historialService.findByMascotaId(mascotaId));
    }
}