package com.odc.Apiodkerp.ServiceImplementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.odc.Apiodkerp.Models.IntervenantExterne;
import com.odc.Apiodkerp.Repository.IntervenantExtRepo;
import com.odc.Apiodkerp.Service.IntervenantExterneService;

@Service
public class IntervenantExterneServiceImpl implements IntervenantExterneService {

    @Autowired
    IntervenantExtRepo intervenantExtRepo;

    @Override
    public IntervenantExterne creer(IntervenantExterne intervenant) {
        // TODO Auto-generated method stub
        return intervenantExtRepo.save(intervenant);
    }

    @Override
    public IntervenantExterne update(IntervenantExterne intervenant) {
        // TODO Auto-generated method stub
        return intervenantExtRepo.save(intervenant);
    }

    @Override
    public void delete(Long id) {
        // TODO Auto-generated method stub
        intervenantExtRepo.deleteById(id);

    }

    @Override
    public IntervenantExterne getById(Long id) {
        // TODO Auto-generated method stub
        return intervenantExtRepo.findById(id).get();
    }

    @Override
    public List<IntervenantExterne> getAll() {
        // TODO Auto-generated method stub
        return intervenantExtRepo.findAll();
    }

    @Override
    public IntervenantExterne getByEmail(String email) {
        // TODO Auto-generated method stub
        return intervenantExtRepo.findByEmail(email);
    }

}
