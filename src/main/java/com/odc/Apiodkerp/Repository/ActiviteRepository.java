package com.odc.Apiodkerp.Repository;

import com.odc.Apiodkerp.Models.Activite;
import com.odc.Apiodkerp.Models.Entite;
import com.odc.Apiodkerp.Models.Etat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Repository
public interface ActiviteRepository extends JpaRepository<Activite, Long> {

    Activite findByEtat(Etat etat);

    Activite findByNom(String nom);
    
    @Query(value = "SELECT COUNT(activite.id) FROM activite", nativeQuery = true)
    public Long Total();

    @Query(value = "SELECT *  FROM activite", nativeQuery = true)
    public Long toutActivite();

    @Query(value = "SELECT * FROM activite ORDER BY id DESC",nativeQuery = true)
    public List<Activite> trouveractivitepar();

    List<Activite> findAllByOrderByIdDesc();

    @Query(value = "SELECT activite.* from activite,entite where entite.utilisateur=activite.createur and entite.id=:identite", nativeQuery = true)
    public List<Activite> actEntite(@PathVariable long identite);

    @Query(
            value = "SELECT act.* FROM activite AS act " +
                    "LEFT JOIN type_activite AS ta " +
                    "ON act.type_activite = ta.id " +
                    "LEFT JOIN utilisateur AS ut " +
                    "ON act.createur = ut.id " +
                    "LEFT JOIN entite AS ent " +
                    "ON ut.mon_entite_id = ent.id " +
                    "WHERE act.nom LIKE '%:nomactivite%' " +
                    "OR ta.libelle LIKE '%:typeactivite%' " +
                    "OR ent.libelleentite LIKE '%:entite%' " +
                    "OR (act.date_debut >= :dtdebut AND act.date_fin <= :dtfin);",
            nativeQuery = true
    )
    List<Activite> getFiltre(String nomactivite, String typeactivite, String entite, String dtdebut, String dtfin);
}
