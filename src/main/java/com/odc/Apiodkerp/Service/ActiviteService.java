package com.odc.Apiodkerp.Service;

import com.odc.Apiodkerp.Models.Activite;
import com.odc.Apiodkerp.Models.Etat;

import java.util.List;

public interface ActiviteService {

    Activite Create(Activite activite);

    List<Activite> GetAll();

    Activite Update(long id, Activite activite);

    String Delete(long id);

    Activite GetById(long id);

    List<Activite> FindAllAct();

    // Activite en fonction de l'etat
    Activite GetByEtat(Etat etat);

    String attribuerSalle(long idsalle, long idactivite);

    Long TotalActivite();

    Long ToutActivite();

    List<Activite> ToutActivit();

    List<Activite> Avenir();

    List<Activite> Encour();

    List<Activite> Termine();

    List<Activite> ActiviteEntiteid(long identite);

    List<Activite> findFiltre(String nomactivite, String typeactivite, String entite, String dtdebut, String dtfin);

    Activite RecupererParNom(String nom);
}
