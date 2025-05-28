package com.veterinaria.vet.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MascotaDto {
    private Long id;
    private String nombre;
    private String especie;
    private String raza;
    private String sexo;
    private Integer edad;
    private Long clienteId;
}