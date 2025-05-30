package com.veterinaria.vet.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FacturaDto {
    private Long id;
    private LocalDateTime fecha;
    private BigDecimal total;
    private Long clienteId;
    private List<Long> consultaIds;
}