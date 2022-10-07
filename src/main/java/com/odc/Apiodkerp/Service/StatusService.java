package com.odc.Apiodkerp.Service;

import java.util.List;

import com.odc.Apiodkerp.Models.Statut;

public interface StatusService {

    Statut creer(Statut status);

    Statut update(Statut status);

    void delete(Long id);

    Statut getById(Long id);

    Statut getByLibelle(String libelle);

    List<Statut> getAll();
}
