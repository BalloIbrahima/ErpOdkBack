package com.odc.Apiodkerp.Service;

import com.odc.Apiodkerp.Models.Activite;

import java.util.List;
import java.util.Optional;

public interface ActiviteService {

    Activite Create(Activite activite);

    List<Activite> GetAll();

    Activite Update(long id,Activite activite);

    String Delete(long id);

    Activite GetById(long id);


    String attribuerSalle(long idsalle, long idactivite);
}
