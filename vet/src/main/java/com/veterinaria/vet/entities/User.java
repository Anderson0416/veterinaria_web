package com.veterinaria.vet.entities;

import java.time.LocalDate;

import jakarta.persistence.*;
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
@Table(name = "usuarios")
public class User {

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "tipo_id", nullable = false)
    @Size(max = 100)
    private String tipo_id;

    @Column(name = "nombre", nullable = false)
    @Size(max = 100)
    private String nombre;

    @Column(name = "apellido", nullable = false)
    @Size(max = 100)
    private String apellido;

    @Column(name = "sexo", nullable = false)
    @Size(max = 100)
    private String sexo;

    @Column(name = "fechaNacimiento", nullable = false)
    private LocalDate  fechaNacimiento;

    @Column(name = "telefono", nullable = false)
    @Size(max = 100)
    private String telefono;

    @Column(name = "fechaContrato", nullable = false)
    private LocalDate  fechaContrato;

    @Column(nullable = false)
    @Size(max = 100)
    private String login;

    @Column(nullable = false)
    @Size(max = 100)
    private String contraseña;

    @Column(name = "foto", nullable = true)
    private String foto;

    // Cambiado de ManyToMany a OneToOne
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rol_id", unique = false)
    private Role rol;

    @Override
    public String toString() {
        return "User{id=" + id + ", nombre='" + nombre + '\'' + ", apellido='" + apellido + '\'' + ", login='" + login + "'}";
    }
}