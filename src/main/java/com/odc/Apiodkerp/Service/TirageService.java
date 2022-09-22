package com.odc.Apiodkerp.Service;

import com.odc.Apiodkerp.Models.Postulant;
import com.odc.Apiodkerp.Models.Tirage;

import java.util.List;

public interface TirageService {
    List<Postulant> creer(Tirage tirage, List<Postulant> listeatirer, long nombre);
    Tirage update(long id, Tirage tirage);
    void delete(long id);
    List<Tirage> getAll();
    Tirage getById(long id);
    Tirage findByLibelle(String libelle);

}
