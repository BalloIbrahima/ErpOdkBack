package com.odc.Apiodkerp.ServiceImplementation;

import com.odc.Apiodkerp.Models.Entite;
import com.odc.Apiodkerp.Repository.EntiteRepository;
import com.odc.Apiodkerp.Service.EntiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EntiteServiceImpl implements EntiteService {

    @Autowired
    public EntiteRepository entiteRepository;

    @Override
    public Entite Create(Entite entite) {
        return entiteRepository.save(entite);
    }

    @Override
    public List<Entite> GetAll() {
        return entiteRepository.findAll();
    }

    @Override
    public Entite Update(long id, Entite entite) {
        // entite.setId(id);
        //Entite ent = entiteRepository.findById(id).orElse(null);
       // return entiteRepository.save(ent);

        return entiteRepository.findById(id)
                .map(entite1 -> {
                    entite1.setDescription(entite.getDescription());
                    entite1.setLibelleentite(entite.getLibelleentite());
                    entite1.setImage(entite.getImage());
                    entite1.setGerant(entite.getGerant());
                    entite1.setCreateur(entite.getCreateur());

                    return entiteRepository.save(entite1);
                }).orElseThrow(() -> new RuntimeException("entite non trouvéé"));

    }

    @Override
    public String Delete(Entite entite) {
        System.out.println(entite.getId());
        //entiteRepository.delete(entite);
        entiteRepository.deleteById(entite.getId());
        return "Supprimer avec succes";
    }

    @Override
    public String Delete1(Long id) {
        entiteRepository.DELETEBYID(id);
        return "supprimer avec succec";
    }

    @Override
    public Entite GetById(long id) {
        return entiteRepository.findById(id).get();
    }

    public Entite GetByLibelle(String libelle) {
        return entiteRepository.findBylibelleentite(libelle);
    }

    @Override
    public String DeleteEntiteById(Long id) {
        return null;
    }
}
