package com.veterinaria.vet.controllers;

import com.veterinaria.vet.dtos.ConsultaDto;
import com.veterinaria.vet.services.ConsultaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/consultas")
@RequiredArgsConstructor
public class ConsultaController {

    private final ConsultaService consultaService;

    @PostMapping
    public ResponseEntity<ConsultaDto> create(@Valid @RequestBody ConsultaDto consultaDto) {
        return new ResponseEntity<>(consultaService.create(consultaDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConsultaDto> update(@PathVariable Long id, @Valid @RequestBody ConsultaDto consultaDto) {
        return ResponseEntity.ok(consultaService.update(id, consultaDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        consultaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConsultaDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(consultaService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<ConsultaDto>> findAll() {
        return ResponseEntity.ok(consultaService.findAll());
    }

    @GetMapping("/factura/{facturaId}")
    public ResponseEntity<List<ConsultaDto>> findByFacturaId(@PathVariable Long facturaId) {
        return ResponseEntity.ok(consultaService.findByFacturaId(facturaId));
    }
}