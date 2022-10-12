package com.odc.Apiodkerp.Repository;

import com.odc.Apiodkerp.Models.AouP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface AouPReposy extends JpaRepository<AouP, Long> {
    @Query(value = "SELECT aoup.* FROM apprenant_participant AS aoup " +
            "LEFT JOIN activite AS act ON act.id=aoup.idactivite " +
            "LEFT JOIN type_activite AS type ON act.type_activite=type.id" +
            " WHERE type.libelle=':typeactivite' OR (act.date_debut >= :datedebut AND act.date_fin <= :datefin);",
            nativeQuery = true)
    List<AouP> filtrer(String typeactivite, String datedebut, String datefin);
}
