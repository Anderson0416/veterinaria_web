package com.veterinaria.vet.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CitaCreateDto {
    // Datos de la factura
    private BigDecimal total;
    private Long clienteId;
    private List<Long> consultaIds;
    
    // Datos de la cita
    private LocalDateTime fecha;
    private String anotaciones;
    private Long mascotaId;
    private Long veterinarioId;
}