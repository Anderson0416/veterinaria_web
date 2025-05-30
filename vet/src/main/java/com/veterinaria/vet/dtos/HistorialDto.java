package com.veterinaria.vet.dtos;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HistorialDto {
    private Long id;
    private LocalDateTime fecha;
    private Long mascotaId;
    private Long anamnesisId;
}
