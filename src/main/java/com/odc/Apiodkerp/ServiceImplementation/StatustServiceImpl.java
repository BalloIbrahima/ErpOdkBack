package com.odc.Apiodkerp.ServiceImplementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.odc.Apiodkerp.Models.Statut;
import com.odc.Apiodkerp.Repository.StatusRepository;
import com.odc.Apiodkerp.Service.StatusService;

@Service
public class StatustServiceImpl implements StatusService {

    @Autowired
    StatusRepository statusRepository;

    @Override
    public Statut creer(Statut status) {
        // TODO Auto-generated method stub
        return statusRepository.save(status);
    }

    @Override
    public Statut update(Statut status) {
        // TODO Auto-generated method stub
        return statusRepository.save(status);
    }

    @Override
    public void delete(Long id) {
        // TODO Auto-generated method stub
        statusRepository.deleteById(id);
        ;
    }

    @Override
    public Statut getById(Long id) {
        // TODO Auto-generated method stub
        return statusRepository.findById(id).get();
    }

    @Override
    public Statut getByLibelle(String libelle) {
        // TODO Auto-generated method stub
        return statusRepository.findByLibelle(libelle);
    }

    @Override
    public List<Statut> getAll() {
        // TODO Auto-generated method stub
        return statusRepository.findAll();
    }

}
