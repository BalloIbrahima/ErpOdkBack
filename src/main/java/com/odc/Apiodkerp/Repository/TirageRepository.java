package com.odc.Apiodkerp.Repository;

import com.odc.Apiodkerp.Models.Tirage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TirageRepository extends JpaRepository<Tirage, Long> {
    Tirage findByLibelle(String libelle);


    @Query(value = "SELECT DISTINCT liste_postulant.libelle FROM tirage,liste_postulant WHERE tirage.idlistepostulant=liste_postulant.id", nativeQuery = true)
    Iterable<Object[]> AfficherListPost(Long idlistepostulant);
}
