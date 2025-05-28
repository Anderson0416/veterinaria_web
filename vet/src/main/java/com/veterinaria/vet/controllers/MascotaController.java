package com.veterinaria.vet.controllers;

import com.veterinaria.vet.dtos.MascotaDto;
import com.veterinaria.vet.services.MascotaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mascotas")
@RequiredArgsConstructor
public class MascotaController {

    private final MascotaService mascotaService;

    @PostMapping
    public ResponseEntity<MascotaDto> create(@Valid @RequestBody MascotaDto mascotaDto) {
        return new ResponseEntity<>(mascotaService.create(mascotaDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MascotaDto> update(@PathVariable Long id, @Valid @RequestBody MascotaDto mascotaDto) {
        return ResponseEntity.ok(mascotaService.update(id, mascotaDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        mascotaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MascotaDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(mascotaService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<MascotaDto>> findAll() {
        return ResponseEntity.ok(mascotaService.findAll());
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<MascotaDto>> findByClienteId(@PathVariable Long clienteId) {
        return ResponseEntity.ok(mascotaService.findByClienteId(clienteId));
    }
}