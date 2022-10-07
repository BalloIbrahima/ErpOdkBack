package com.odc.Apiodkerp.Service;

import com.odc.Apiodkerp.Models.Salle;

import java.util.List;

public interface SalleService {

    // Creer Salle
    Salle create(Salle salle);

    // Afficher une salle par son id
    Salle read(long id);

    // Modifier salle par id
    Salle update(Salle salle, long id);

    // Effacer salle
    void delete(long id);

    List<Salle> getAll();

    // Afficher salle par libelle
    Salle getByLibelle(String libelle);
    Salle getByIdsalle(long id);

    /// list des salles par disponiblite
    List<Salle> ParEtat(Boolean disponibilite);

}
