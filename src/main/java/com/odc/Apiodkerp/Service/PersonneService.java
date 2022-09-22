package com.odc.Apiodkerp.Service;

import com.odc.Apiodkerp.Models.ListePostulant;
import com.odc.Apiodkerp.Models.Personne;

import java.util.List;

public interface PersonneService {
    // Création d'une personne
    Personne creer(Personne personne);

    // Mettre à jour de la personne
    Personne update(Long id, Personne personne);

    // l'ensemble des personnes
    List<Personne> getAll();

    // Retrouver la personnes à travers son id
    Personne GetById(Long id);

    //Récuperer l'email de la personne
    Personne GetByEmail(String email);
}
