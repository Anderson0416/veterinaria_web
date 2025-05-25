package com.veterinaria.vet.dtos;

import java.time.LocalDate;
import io.micrometer.common.lang.Nullable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignUpDto {

    @NotNull
    private Long id;

    @NotEmpty
    private String tipo_id;

    @NotEmpty
    private String nombre;

    @NotEmpty
    private String apellido;

    @NotEmpty
    private String sexo;

    @NotNull
    private LocalDate fechaNacimiento;

    @NotEmpty
    private String telefono;

    @NotNull
    private LocalDate fechaContrato;

    @NotEmpty
    private String login;

    @NotEmpty
    private char[] contraseña;
    
    @Nullable
    private String foto;
    
    @NotNull
    private RoleDto rol;
}