package com.odc.Apiodkerp.ServiceImplementation;

import com.odc.Apiodkerp.Models.Designation;
import com.odc.Apiodkerp.Repository.DesignationRepos;
import com.odc.Apiodkerp.Service.DesignationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DesignationServiceImplement implements DesignationService {

    @Autowired
    DesignationRepos desirepos;
    @Override
    public Designation Create(Designation desi) {
        return desirepos.save(desi);
    }

    @Override
    public List<Designation> GetAll() {
        return desirepos.findAll();
    }

    @Override
    public Designation Update(long id, Designation desi) {

        Designation des = desirepos.findById(id).orElse(null);
        return desirepos.save(desi);
    }

    @Override
    public String Delete(long id) {
        desirepos.deleteById(id);
        return "Designation supprimer avec succes";
    }

    @Override
    public Designation GetById(long id) {
        return desirepos.findById(id).get();
    }
}
