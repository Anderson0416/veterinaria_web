package com.veterinaria.vet.controllers;

import com.veterinaria.vet.dtos.FacturaDto;
import com.veterinaria.vet.services.FacturaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/facturas")
@RequiredArgsConstructor
public class FacturaController {

    private final FacturaService facturaService;

    @PostMapping
    public ResponseEntity<FacturaDto> create(@Valid @RequestBody FacturaDto facturaDto) {
        return new ResponseEntity<>(facturaService.create(facturaDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FacturaDto> update(@PathVariable Long id, @Valid @RequestBody FacturaDto facturaDto) {
        return ResponseEntity.ok(facturaService.update(id, facturaDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        facturaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<FacturaDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(facturaService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<FacturaDto>> findAll() {
        return ResponseEntity.ok(facturaService.findAll());
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<FacturaDto>> findByClienteId(@PathVariable Long clienteId) {
        return ResponseEntity.ok(facturaService.findByClienteId(clienteId));
    }
}