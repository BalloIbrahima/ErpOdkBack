package com.odc.Apiodkerp.Repository;

import com.odc.Apiodkerp.Models.Entite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

@Repository
public interface EntiteRepository extends JpaRepository<Entite, Long> {

    Entite  findBylibelleentite(String libelle);

    //Methode permettant de supprimer une entite
    @Query(value = "DELETE FROM entite WHERE :id",nativeQuery = true)
    public void deleteEntiteById(@Param("id") Long id);
}
