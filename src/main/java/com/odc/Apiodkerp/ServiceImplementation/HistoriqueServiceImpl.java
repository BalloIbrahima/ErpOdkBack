package com.odc.Apiodkerp.ServiceImplementation;

import com.odc.Apiodkerp.Models.Historique;
import com.odc.Apiodkerp.Repository.HistoriqueRepo;
import com.odc.Apiodkerp.Service.HistoriqueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class HistoriqueServiceImpl implements HistoriqueService {
    @Autowired
    HistoriqueRepo historiqueRepo;

    @Override
    public Historique Create(Historique historique) {
        return historiqueRepo.save(historique);
    }

    @Override
    public List<Historique> GetAll() {
        return historiqueRepo.findAll();
    }

    @Override
    public Historique Update(long id, Historique historique) {
        Historique hist = historiqueRepo.findById(id).orElse(null);
        return historiqueRepo.save(historique);
    }

    @Override
    public String Delete(long id) {
        historiqueRepo.deleteById(id);
        return "Supprimer avec succes";
    }

    @Override
    public Historique GetById(long id) {
        return historiqueRepo.findById(id).get();
    }

}
