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
public class CitaResponseDto {
    private Long id;
    private String estado;
    private Boolean atendido;
    private LocalDateTime fecha;
    private String anotaciones;
    private ClienteDto cliente;
    private MascotaDto mascota;
    private UserDto veterinario;
    private FacturaDto factura;
}