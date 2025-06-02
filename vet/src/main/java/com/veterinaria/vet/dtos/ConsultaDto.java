package com.veterinaria.vet.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConsultaDto {
    private Long id;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
}