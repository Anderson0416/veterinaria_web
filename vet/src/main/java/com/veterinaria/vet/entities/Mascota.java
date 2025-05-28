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
@Table(name = "mascotas")
public class Mascota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Size(max = 100)
    @Column(name = "especie", nullable = true)
    private String especie;

    @Size(max = 100)
    @Column(name = "raza", nullable = true)
    private String raza;

    @Size(max = 50)
    @Column(name = "sexo", nullable = true)
    private String sexo;

    @Column(name = "edad", nullable = true)
    private Integer edad;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;
}