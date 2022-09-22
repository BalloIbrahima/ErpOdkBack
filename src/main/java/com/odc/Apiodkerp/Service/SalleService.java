package com.odc.Apiodkerp.Service;

import com.odc.Apiodkerp.Models.Salle;

import java.util.List;

public interface SalleService {

    // Creer Salle
    Salle create(Salle salle);
    //Afficher salle par libelle
    Salle read(String libelle);
    //Modifier salle par id
    Salle update(Salle salle,long id);
    //Effacer salle
    void delete(long id);

    List<Salle> getAll();

}
