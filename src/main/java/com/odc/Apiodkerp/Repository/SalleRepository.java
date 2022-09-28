package com.odc.Apiodkerp.Repository;

import com.odc.Apiodkerp.Models.Salle;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalleRepository extends JpaRepository<Salle, Long> {
    Salle findByLibelle(String libelle);

    List<Salle> findByDisponibilite(Boolean disponibilite);
}
