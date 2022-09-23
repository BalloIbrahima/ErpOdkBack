package com.odc.Apiodkerp.Repository;

import com.odc.Apiodkerp.Models.Etat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EtatRepository extends JpaRepository<Etat, Long> {

    Etat findByStatut(String statut);
}
