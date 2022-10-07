package com.odc.Apiodkerp.Repository;

import com.odc.Apiodkerp.Models.Activite;
import com.odc.Apiodkerp.Models.Entite;
import com.odc.Apiodkerp.Models.Etat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Repository
public interface ActiviteRepository extends JpaRepository<Activite, Long> {

    Activite findByEtat(long etat);

    @Query(value = "SELECT COUNT(activite.id) FROM activite", nativeQuery = true)
    public Long Total();

    @Query(value = "SELECT *  FROM activite", nativeQuery = true)
    public Long toutActivite();


    @Query(value = "SELECT activite.* from activite,entite where entite.utilisateur=activite.createur and entite.id=:identite", nativeQuery = true)
    public List<Activite> actEntite(@PathVariable long identite);

}
