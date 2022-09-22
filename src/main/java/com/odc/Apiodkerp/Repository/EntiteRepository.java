package com.odc.Apiodkerp.Repository;

import com.odc.Apiodkerp.Models.Entite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntiteRepository extends JpaRepository<Entite, Long> {

    Entite  findBylibelleentite(String libelle);
}
