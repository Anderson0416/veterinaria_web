package com.veterinaria.vet.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "anamnesis")
public class Anamnesis {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "peso", nullable = false)
    private Integer peso;

    @Size(max = 100)
    @Column(name = "estado_reproductivo")
    private String estadoReproductivo;

    @Size(max = 100)
    @Column(name = "tipo_vivienda")
    private String tipoVivienda;

    @Size(max = 100)
    @Column(name = "actividad_fisica")
    private String actividadFisica;

    @Size(max = 500)
    @Column(name = "vacunas_previas")
    private String vacunasPrevias;

    @Size(max = 500)
    @Column(name = "vacunas_previas_desparacitacion")
    private String vacunasPreviasDesparacitacion;

    @Size(max = 500)
    @Column(name = "motivo_consulta")
    private String motivoConsulta;

    @Size(max = 500)
    @Column(name = "sintomas_mascota")
    private String sintomasMascota;

    @Size(max = 1000)
    @Column(name = "observaciones")
    private String observaciones;

    @Size(max = 500)
    @Column(name = "dieta")
    private String dieta;

    @OneToOne(mappedBy = "anamnesis")
    private Historial historial;
}