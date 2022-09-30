package com.odc.Apiodkerp.ServiceImplementation;

import java.util.List;

import com.odc.Apiodkerp.Models.AouP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.odc.Apiodkerp.Models.Activite;
import com.odc.Apiodkerp.Models.PostulantTire;
import com.odc.Apiodkerp.Models.Presence;
import com.odc.Apiodkerp.Repository.PresenceRepository;
import com.odc.Apiodkerp.Service.PresenceService;

@Service
public class PresenceServiceImpl implements PresenceService {
    @Autowired
    PresenceRepository repos;

    @Override
    public Presence creer(Presence presence) {
        // TODO Auto-generated method stub
        return repos.save(presence);
    }

    @Override
    public Presence update(Presence presence) {
        // TODO Auto-generated method stub
        return repos.save(presence);
    }

    @Override
    public void delete(Long id) {
        // TODO Auto-generated method stub
        repos.deleteById(id);

    }



    @Override
    public Presence getById(Long id) {
        // TODO Auto-generated method stub
        return repos.findById(id).get();
    }

    @Override
    public Presence getByaoup(AouP aoup) {
        return repos.findByAouP(aoup);
    }

    @Override
    public List<Presence> getAll() {
        // TODO Auto-generated method stub
        return repos.findAll();
    }


    @Override
    public List<Presence> getByActivite(Activite activite) {
        // TODO Auto-generated method stub
        return repos.findByActivite(activite);
    }

}
