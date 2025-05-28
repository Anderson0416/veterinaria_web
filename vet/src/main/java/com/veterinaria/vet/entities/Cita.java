package com.veterinaria.vet.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "citas")
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotNull
    @Column(name = "estado", nullable = false)
    @Size(max = 20)
    private String estado; 

    @NotNull
    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;

    @Column(name = "anotaciones")
    @Size(max = 500)
    private String anotaciones;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "mascota_id", nullable = false)
    private Mascota mascota;

    @ManyToOne
    @JoinColumn(name = "veterinario_id", nullable = false)
    private User veterinario;

    @OneToOne
    @JoinColumn(name = "factura_id")
    private Factura factura;

    @Builder.Default
    @Column(name = "atendido", nullable = true)
    private Boolean atendido = false; // valor por defecto false
}