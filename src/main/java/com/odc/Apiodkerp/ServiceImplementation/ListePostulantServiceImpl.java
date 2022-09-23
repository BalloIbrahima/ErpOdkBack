package com.odc.Apiodkerp.ServiceImplementation;

import com.odc.Apiodkerp.Models.ListePostulant;
import com.odc.Apiodkerp.Repository.ListePostulantRepository;
import com.odc.Apiodkerp.Service.ListePostulantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListePostulantServiceImpl implements ListePostulantService {
    @Autowired
    ListePostulantRepository listePostulantRepository;

    @Override
    public ListePostulant creer(ListePostulant listepostulant) {
        return listePostulantRepository.save(listepostulant);
    }

    @Override
    public ListePostulant update(Long id, ListePostulant listepostulant) {
        return listePostulantRepository.findById(id)
                .map(p -> {
                    p.setLibelle(listepostulant.getLibelle());
                    p.setDateimport(listepostulant.getDateimport());
                    return listePostulantRepository.save(p);
                }).orElseThrow(() -> new RuntimeException("liste non trouvé !"));
    }

    @Override
    public List<ListePostulant> getAll() {
        return listePostulantRepository.findAll();
    }

    @Override
    public ListePostulant GetById(Long id) {
        return listePostulantRepository.findById(id).get();
    }

    @Override
    public ListePostulant retrouveParLibelle(String libelle) {
        return listePostulantRepository.findByLibelle(libelle);
    }
}
