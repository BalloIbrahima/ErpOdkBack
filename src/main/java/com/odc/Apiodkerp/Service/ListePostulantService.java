package com.odc.Apiodkerp.Service;

import com.odc.Apiodkerp.Models.ListePostulant;

import java.util.List;

public interface ListePostulantService {
    // Création d'une liste de postulant
    ListePostulant creer(ListePostulant listepostulant);

    // Mettre à jour de la liste postulant
    ListePostulant update(Long id, ListePostulant listepostulant);


    // Récuperer l'ensemble des listes
    List<ListePostulant> getAll();

    // Retrouver la liste à travers son id
    ListePostulant GetById(Long id);

    //Retrouver le libelle de la liste
    ListePostulant retrouveParLibelle(String libelle);


}
