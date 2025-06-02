package com.veterinaria.vet.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnamnesisCreateDto {
    private Integer peso;
    private String estadoReproductivo;
    private String tipoVivienda;
    private String actividadFisica;
    private String vacunasPrevias;
    private String vacunasPreviasDesparacitacion;
    private String motivoConsulta;
    private String sintomasMascota;
    private String observaciones;
    private String dieta;
    private Long mascotaId;
}
