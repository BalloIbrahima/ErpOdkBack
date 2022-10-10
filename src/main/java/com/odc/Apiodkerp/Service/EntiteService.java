package com.odc.Apiodkerp.Service;

import com.odc.Apiodkerp.Models.Activite;
import com.odc.Apiodkerp.Models.Entite;
import com.odc.Apiodkerp.Repository.EntiteRepository;

import java.util.List;

public interface EntiteService {

    Entite Create(Entite entite);

    List<Entite> GetAll();

    Entite Update(long id,Entite entite);

    String Delete(Entite entite);

    String Delete1(Long id);

    Entite GetById(long id);

    Entite GetByLibelle(String libelle);

    String DeleteEntiteById(Long id);
}