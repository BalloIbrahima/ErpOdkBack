package com.odc.Apiodkerp.ServiceImplementation;

import com.odc.Apiodkerp.Models.AouP;
import com.odc.Apiodkerp.Models.Entite;
import com.odc.Apiodkerp.Repository.ActiviteRepository;
import com.odc.Apiodkerp.Repository.AouPReposy;
import com.odc.Apiodkerp.Service.AouPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AouPServImpleme implements AouPService {

    @Autowired
    AouPReposy aouprepos;

    @Override
    public AouP Create(AouP aoup) {
        return aouprepos.save(aoup);
    }

    @Override
    public List<AouP> GetAll() {
        return aouprepos.findAll();
    }

    @Override
    public AouP Update(long id, AouP aoup) {

        AouP aouP = aouprepos.findById(id).orElse(null);
        return aouprepos.save(aouP);
    }

    @Override
    public String Delete(long id) {
        aouprepos.deleteById(id);
        return "Supprimer avec succes";
    }

    @Override
    public AouP GetById(long id) {
        return aouprepos.findById(id).get();
    }

}
