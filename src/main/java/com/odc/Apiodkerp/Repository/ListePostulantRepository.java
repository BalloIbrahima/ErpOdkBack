package com.odc.Apiodkerp.Repository;

import com.odc.Apiodkerp.Models.ListePostulant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ListePostulantRepository extends JpaRepository<ListePostulant, Long> {
    ListePostulant findByLibelle(String libelle);
}
