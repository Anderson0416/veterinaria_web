package com.veterinaria.vet.repositories;

import com.veterinaria.vet.entities.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
    @Query("SELECT c FROM Consulta c JOIN c.facturas f WHERE f.id = :facturaId")
    List<Consulta> findByFacturaId(@Param("facturaId") Long facturaId);
}