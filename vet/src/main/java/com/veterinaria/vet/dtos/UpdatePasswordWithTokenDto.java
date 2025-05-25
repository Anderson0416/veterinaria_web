package com.veterinaria.vet.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdatePasswordWithTokenDto {

    @NotBlank
    private String token;

    @NotBlank
    private String newPassword;
}

