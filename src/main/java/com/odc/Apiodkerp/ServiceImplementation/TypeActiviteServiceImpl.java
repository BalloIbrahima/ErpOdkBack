package com.odc.Apiodkerp.ServiceImplementation;

import com.odc.Apiodkerp.Models.TypeActivite;
import com.odc.Apiodkerp.Repository.TypeActiviteRepository;
import com.odc.Apiodkerp.Service.TypeActiviteService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TypeActiviteServiceImpl implements TypeActiviteService {

    @Autowired
    TypeActiviteRepository repos;

    @Override
    public TypeActivite creer(TypeActivite typeActivite) {
        // TODO Auto-generated method stub
        return repos.save(typeActivite);
    }

    @Override
    public TypeActivite update(TypeActivite typeActivite) {
        // TODO Auto-generated method stub
        return repos.save(typeActivite);
    }

    @Override
    public String delete(Long id) {
        // TODO Auto-generated method stub
        repos.deleteById(id);
return "supprimer avec succes";
    }

    @Override
    public TypeActivite getById(Long id) {
        // TODO Auto-generated method stub
        return repos.findById(id).get();
    }

    @Override
    public List<TypeActivite> getAll() {
        // TODO Auto-generated method stub
        return repos.findAll();
    }

    @Override
    public TypeActivite getByLibelle(String libelle) {
        // TODO Auto-generated method stub
        return repos.findByLibelle(libelle);
    }
}
