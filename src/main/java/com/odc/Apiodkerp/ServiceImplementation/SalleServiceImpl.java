package com.odc.Apiodkerp.ServiceImplementation;

import com.odc.Apiodkerp.Models.Salle;
import com.odc.Apiodkerp.Repository.SalleRepository;
import com.odc.Apiodkerp.Service.SalleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SalleServiceImpl implements SalleService {

    @Autowired
    SalleRepository salleRepository;

    @Override
    public Salle create(Salle salle) {
        return salleRepository.save(salle);
    }

    @Override
    public Salle read(long id) {
        return salleRepository.findById(id).get();
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

    @Override
    public Salle getByLibelle(String libelle) {
        return salleRepository.findByLibelle(libelle);
    }

    @Override
    public Salle getByIdsalle(long id) {
        return salleRepository.findById(id).get();
    }

    @Override
    public List<Salle> ParEtat(Boolean disponibilite) {
        // TODO Auto-generated method stub
        return salleRepository.findByDisponibilite(disponibilite);
    }

}
