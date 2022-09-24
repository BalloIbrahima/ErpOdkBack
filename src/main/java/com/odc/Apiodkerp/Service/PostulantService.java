package com.odc.Apiodkerp.Service;

import com.odc.Apiodkerp.Models.Personne;
import com.odc.Apiodkerp.Models.Postulant;

import java.util.List;

public interface PostulantService {
    // Création d'un postulant
    Postulant creer(Postulant postulant);

    // Mettre à jour d'un postulant
    Postulant update(Long id, Postulant postulant);

    // Afficher l'ensemble des postulants
    List<Postulant> getAll();

    // Retrouver les postulants à travers son id
    Postulant GetById(Long id);

    // Retrouver le postulant par email
    Postulant GetByEmail(String email);



}
