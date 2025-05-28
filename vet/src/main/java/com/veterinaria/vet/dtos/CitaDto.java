package com.veterinaria.vet.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CitaDto {
    private Long id;
    private String estado;
    private LocalDateTime fecha;
    private String anotaciones;
    private Long clienteId;
    private Long mascotaId;
    private Long veterinarioId;
    private Long facturaId;
}

