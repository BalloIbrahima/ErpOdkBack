package com.odc.Apiodkerp.ServiceImplementation;

import com.odc.Apiodkerp.Models.Salle;
import com.odc.Apiodkerp.Repository.SalleRepository;
import com.odc.Apiodkerp.Service.SalleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SalleServiceImpl implements SalleService {

    private SalleRepository salleRepository;
    @Override
    public Salle create(Salle salle) {
        return salleRepository.save(salle);
    }

    @Override
    public Salle read(String libelle) {
        return salleRepository.findByLibelle(libelle);
    }

    @Override
    public Salle update(Salle salle, long id) {
        return salleRepository.save(salle);
    }

    @Override
    public void delete(long id) {
        salleRepository.deleteById(id);
    }

    @Override
    public List<Salle> getAll() {
        return salleRepository.findAll();
    }

}
