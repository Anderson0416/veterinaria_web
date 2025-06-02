package com.veterinaria.vet.repositories;

import com.veterinaria.vet.entities.Anamnesis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnamnesisRepository extends JpaRepository<Anamnesis, Long> {
}