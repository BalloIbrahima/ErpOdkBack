package com.odc.Apiodkerp.ServiceImplementation;

import com.odc.Apiodkerp.Models.Personne;
import com.odc.Apiodkerp.Repository.PersonneRepository;
import com.odc.Apiodkerp.Service.PersonneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonneServiceImpl implements PersonneService {

    @Autowired
    PersonneRepository personneRepository;
    @Override
    public Personne creer(Personne personne) {
        return personneRepository.save(personne);
    }

    @Override
    public Personne update(Long id, Personne personne) {
        return personneRepository.findById(id)
                .map(p->{
                    p.setNom(personne.getNom());
                    p.setPrenom(personne.getPrenom());
                    p.setEmail(personne.getEmail());
                    p.setGenre(personne.getGenre());
                    return personneRepository.save(p);
                }).orElseThrow(()-> new RuntimeException("Personne non trouvé !"));
    }

    @Override
    public List<Personne> getAll() {
        return personneRepository.findAll();
    }

    @Override
    public Personne GetById(Long id) {
        return personneRepository.findById(id).get();
    }

    @Override
    public Personne GetByEmail(String email) {
        return personneRepository.findByEmail(email);
    }
}
