package com.odc.Apiodkerp.Repository;

import com.odc.Apiodkerp.Models.Entite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EntiteRepository extends JpaRepository<Entite, Long> {

    Entite  findBylibelleentite(String libelle);

    @Query(value = "DELETE FROM entite WHERE id = :id",nativeQuery = true)
    public String DELETEBYID(@Param("id") Long id);
}
