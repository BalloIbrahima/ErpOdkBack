package com.odc.Apiodkerp.ServiceImplementation;

import com.odc.Apiodkerp.Models.Activite;
import com.odc.Apiodkerp.Models.Entite;
import com.odc.Apiodkerp.Repository.EntiteRepository;
import com.odc.Apiodkerp.Service.EntiteService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EntiteServiceImpl implements EntiteService {

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
       Entite ent =  entiteRepository.findById(id).orElse(null);
        return entiteRepository.save(ent);
    }

    @Override
    public String Delete(long id) {
        entiteRepository.deleteById(id);
        return "Suppreimer avec succes";
    }

    @Override
    public Entite GetById(long id) {
        return entiteRepository.findById(id).get();
    }

    public Entite GetByLibelle(String libelle) {
        return entiteRepository.findBylibelleentite(libelle);
    }
}
