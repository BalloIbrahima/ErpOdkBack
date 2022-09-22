package com.odc.Apiodkerp.Repository;

import com.odc.Apiodkerp.Models.Salle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalleRepository extends JpaRepository<Salle, Long> {
    Salle findByLibelle(String libelle);
}
