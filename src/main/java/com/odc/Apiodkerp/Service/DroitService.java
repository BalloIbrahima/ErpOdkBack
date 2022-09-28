package com.odc.Apiodkerp.Service;

import com.odc.Apiodkerp.Models.Droit;

import java.util.List;

public interface DroitService {


    Droit Create(Droit droit);

    List<Droit> GetAll();

    Droit Update(long id, Droit droit);

    String Delete(long id);

    Droit GetById(long id);
    Droit GetLibelle(String libelle);


}
