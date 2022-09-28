package com.odc.Apiodkerp.Service;

import com.odc.Apiodkerp.Models.Activite;
import com.odc.Apiodkerp.Models.AouP;
import com.odc.Apiodkerp.Models.Etat;

import java.util.List;

public interface AouPService {


    AouP Create(AouP aoup);

    List<AouP> GetAll();

    AouP Update(long id, AouP aoup);

    String Delete(long id);

    AouP GetById(long id);



    // Activite en fonction de l'etat




}