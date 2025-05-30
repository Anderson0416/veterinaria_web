package com.veterinaria.vet.repositories;

import com.veterinaria.vet.entities.Historial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistorialRepository extends JpaRepository<Historial, Long> {
    List<Historial> findByMascotaId(Long mascotaId);
}