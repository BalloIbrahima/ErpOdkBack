package com.odc.Apiodkerp.ServiceImplementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.odc.Apiodkerp.Models.Tache;
import com.odc.Apiodkerp.Repository.TacheRepository;
import com.odc.Apiodkerp.Service.TacheService;

@Service
public class TacheServiveImpl implements TacheService {

    @Autowired
    TacheRepository tacheRepository;

    @Override
    public Tache creer(Tache tache) {
        // TODO Auto-generated method stub
        return tacheRepository.save(tache);
    }

    @Override
    public Tache update(Tache tache) {
        // TODO Auto-generated method stub
        return tacheRepository.save(tache);
    }

    @Override
    public void delete(Long id) {
        // TODO Auto-generated method stub
        tacheRepository.deleteById(id);

    }

    @Override
    public Tache getById(Long id) {
        // TODO Auto-generated method stub
        return tacheRepository.findById(id).get();
    }

    @Override
    public List<Tache> getAll() {
        // TODO Auto-generated method stub
        return tacheRepository.findAll();
    }

}
