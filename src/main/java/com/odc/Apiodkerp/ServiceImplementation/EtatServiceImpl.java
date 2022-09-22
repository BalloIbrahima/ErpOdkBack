package com.odc.Apiodkerp.ServiceImplementation;

import com.odc.Apiodkerp.Models.Etat;
import com.odc.Apiodkerp.Repository.EtatRepository;
import com.odc.Apiodkerp.Service.EtatService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EtatServiceImpl implements EtatService {

    public EtatRepository etatRepository;
    @Override
    public Etat Create(Etat etat) {
        return etatRepository.save(etat);
    }

    @Override
    public List<Etat> GetAll() {
        return etatRepository.findAll();
    }

    @Override
    public Etat Update(long id, Etat etat) {

        Etat et = etatRepository.findById(id).orElse(null);
        return etatRepository.save(et);
    }

    @Override
    public String Delete(long id) {
        etatRepository.deleteById(id);
        return " Supprimé avec succes";
    }

    @Override
    public Etat GetById(long id) {
        return etatRepository.findById(id).get();
    }
}
