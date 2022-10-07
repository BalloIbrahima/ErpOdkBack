package com.odc.Apiodkerp.ServiceImplementation;

import com.odc.Apiodkerp.Models.Droit;
import com.odc.Apiodkerp.Repository.DroitReposy;
import com.odc.Apiodkerp.Service.DroitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class DroitServiceImplement implements DroitService {

    @Autowired
    DroitReposy droitReposy;

    @Override
    public Droit Create(Droit droit) {
        return droitReposy.save(droit);
    }

    @Override
    public List<Droit> GetAll() {
        return droitReposy.findAll();
    }

    @Override
    public Droit Update(long id, Droit droit) {

        Droit dt = droitReposy.findById(id).orElse(null);
        return droitReposy.save(dt);
    }

    @Override
    public String Delete(long id) {
        droitReposy.deleteById(id);
        return "Droit supprimé avec succes";
    }

    @Override
    public Droit GetById(long id) {
        return droitReposy.findById(id).get();
    }

    @Override
    public Droit GetLibelle(String libelle) {
        return droitReposy.findByLibelle(libelle);
    }
}
