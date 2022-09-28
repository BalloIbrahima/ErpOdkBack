package com.odc.Apiodkerp.Service;

import java.util.List;

import com.odc.Apiodkerp.Models.Tache;

public interface TacheService {

    Tache creer(Tache tache);

    Tache update(Tache tache);

    void delete(Long id);

    Tache getById(Long id);

    List<Tache> getAll();
}
