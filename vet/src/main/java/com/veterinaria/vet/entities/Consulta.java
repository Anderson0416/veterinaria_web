package com.veterinaria.vet.entities;

import java.util.ArrayList;
import java.util.List;

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
@Table(name = "Consulta")
public class Consulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Size(max = 500)
    @Column(name = "descripcion")
    private String descripcion;

    @NotNull
    @Column(name = "precio", nullable = false)
    private Long precio;
    
    @ManyToMany(mappedBy = "consultas")
    @Builder.Default
    private List<Factura> facturas = new ArrayList<>();
}
