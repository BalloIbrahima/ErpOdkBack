package com.odc.Apiodkerp.Service;

import com.odc.Apiodkerp.Models.Historique;

import java.util.List;

public interface HistoriqueService {

    Historique Create(Historique historique);

    List<Historique> GetAll();

    Historique Update(long id, Historique historique);

    String Delete(long id);

    Historique GetById(long id);

}
