package com.odc.Apiodkerp.ServiceImplementation;

import com.odc.Apiodkerp.Models.Entite;
import com.odc.Apiodkerp.Repository.EntiteRepository;
import com.odc.Apiodkerp.Service.EntiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EntiteServiceImpl implements EntiteService {

    @Autowired
    public EntiteRepository entiteRepository;

    @Override
    public Entite Create(Entite entite) {
        return entiteRepository.save(entite);
    }

    @Override
    public List<Entite> GetAll() {
        return entiteRepository.findAll();
    }

    @Override
    public Entite Update(long id, Entite entite) {
        // entite.setId(id);
        Entite ent = entiteRepository.findById(id).orElse(null);
        return entiteRepository.save(ent);
    }

    @Override
    public String Delete(long id) {

        entiteRepository.deleteEntiteById(id);
        return "Suppreimer avec succes";
    }

    @Override
    public Entite GetById(long id) {
        return entiteRepository.findById(id).get();
    }

    public Entite GetByLibelle(String libelle) {
        return entiteRepository.findBylibelleentite(libelle);
    }

    @Override
    public String DeleteEntiteById(long id) {
        return entiteRepository.deleteEntiteById(id);
    }
}
