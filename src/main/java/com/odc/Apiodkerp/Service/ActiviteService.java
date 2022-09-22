package com.odc.Apiodkerp.Service;

import com.odc.Apiodkerp.Models.Activite;
import com.odc.Apiodkerp.Models.Etat;

import java.util.List;
import java.util.Optional;

public interface ActiviteService {

    Activite Create(Activite activite);

    List<Activite> GetAll();

    Activite Update(long id,Activite activite);

    String Delete(long id);

    Activite GetById(long id);

//Activite en fonction de l'etat
    Activite GetByEtat( Etat etat);




}
