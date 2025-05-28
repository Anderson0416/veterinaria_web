package com.veterinaria.vet.controllers;

import com.veterinaria.vet.dtos.CitaDto;
import com.veterinaria.vet.dtos.CitaResponseDto;
import com.veterinaria.vet.dtos.CitaCreateDto;
import com.veterinaria.vet.services.CitaService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/citas")
@RequiredArgsConstructor
public class CitaController {
    private final CitaService citaService;

    @PostMapping("/with-factura")
    public ResponseEntity<CitaDto> createWithFactura(@Valid @RequestBody CitaCreateDto citaCreateDto) {
        System.out.println("Creating Cita with Factura: " + citaCreateDto);
        return new ResponseEntity<CitaDto>(citaService.createWithFactura(citaCreateDto), HttpStatus.CREATED);
    }

    @PostMapping
    public ResponseEntity<CitaDto> create(@Valid @RequestBody CitaDto citaDto) {
        return new ResponseEntity<>(citaService.create(citaDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CitaDto> update(@PathVariable Long id, @Valid @RequestBody CitaDto citaDto) {
        return ResponseEntity.ok(citaService.update(id, citaDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        citaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CitaResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(citaService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<CitaResponseDto>> findAll() {
        return ResponseEntity.ok(citaService.findAll());
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<CitaResponseDto>> findByClienteId(@PathVariable Long clienteId) {
        return ResponseEntity.ok(citaService.findByClienteId(clienteId));
    }

    @GetMapping("/veterinario/{veterinarioId}")
    public ResponseEntity<List<CitaResponseDto>> findByVeterinarioId(@PathVariable Long veterinarioId) {
        return ResponseEntity.ok(citaService.findByVeterinarioId(veterinarioId));
    }

    @GetMapping("/mascota/{mascotaId}")
    public ResponseEntity<List<CitaResponseDto>> findByMascotaId(@PathVariable Long mascotaId) {
        return ResponseEntity.ok(citaService.findByMascotaId(mascotaId));
    }

    @PutMapping("/{id}/pagar")
    public ResponseEntity<CitaDto> pagarCita(@PathVariable Long id) {
        try {
            CitaDto citaActualizada = citaService.pagarCita(id);
            return ResponseEntity.ok(citaActualizada);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PutMapping("/{id}/atender")
    public ResponseEntity<CitaDto> marcarComoAtendida(@PathVariable Long id) {
        try {
            CitaDto citaActualizada = citaService.marcarComoAtendida(id);
            return ResponseEntity.ok(citaActualizada);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}