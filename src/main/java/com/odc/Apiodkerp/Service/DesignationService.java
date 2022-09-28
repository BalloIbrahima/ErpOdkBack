package com.odc.Apiodkerp.Service;

import com.odc.Apiodkerp.Models.AouP;
import com.odc.Apiodkerp.Models.Designation;

import java.util.List;

public interface DesignationService {

    Designation Create(Designation desi);

    List<Designation> GetAll();

    Designation Update(long id, Designation desi);

    String Delete(long id);

    Designation GetById(long id);



}
