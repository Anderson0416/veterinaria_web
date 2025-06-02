package com.veterinaria.vet.services;

import com.veterinaria.vet.dtos.AnamnesisCreateDto;
import com.veterinaria.vet.dtos.AnamnesisDto;

public interface AnamnesisService {
    AnamnesisDto createWithHistorial(AnamnesisCreateDto anamnesisCreateDto);
    // ...otros métodos CRUD
}
