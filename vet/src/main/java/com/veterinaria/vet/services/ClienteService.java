package com.veterinaria.vet.services;

import com.veterinaria.vet.dtos.ClienteDto;
import java.util.List;

public interface ClienteService {
    ClienteDto create(ClienteDto clienteDto);
    ClienteDto update(Long id, ClienteDto clienteDto);
    void delete(Long id);
    ClienteDto findById(Long id);
    List<ClienteDto> findAll();
}