package com.odc.Apiodkerp.Service;

import com.odc.Apiodkerp.Models.Etat;

import java.util.List;

public interface EtatService {

    Etat Create(Etat etat);

    List<Etat> GetAll();

    Etat Update(long id, Etat etat);

    String Delete(long id);

    Etat GetById(long id);

    Etat recupereParStatut(String statut);
}
