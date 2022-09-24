package com.odc.Apiodkerp.Repository;

import com.odc.Apiodkerp.Models.Activite;
import com.odc.Apiodkerp.Models.Entite;
import com.odc.Apiodkerp.Models.Etat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActiviteRepository extends JpaRepository<Activite, Long> {

    Activite findByEtat(Etat etat);

    @Query(value = "SELECT COUNT(activite.id) FROM activite", nativeQuery = true)
    public Long Total();
}
