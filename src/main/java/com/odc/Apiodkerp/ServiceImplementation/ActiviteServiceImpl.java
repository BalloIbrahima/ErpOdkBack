package com.odc.Apiodkerp.ServiceImplementation;

import com.odc.Apiodkerp.Models.Activite;
import com.odc.Apiodkerp.Models.Salle;

import com.odc.Apiodkerp.Models.Etat;

import com.odc.Apiodkerp.Repository.ActiviteRepository;
import com.odc.Apiodkerp.Repository.SalleRepository;
import com.odc.Apiodkerp.Service.ActiviteService;
import com.odc.Apiodkerp.Service.SalleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ActiviteServiceImpl implements ActiviteService {
    @Autowired
    SalleRepository salleRepository;

    @Autowired
    ActiviteRepository activiteRepository;

    @Override
    public Activite Create(Activite activite) {
        return activiteRepository.save(activite);
    }

    @Override
    public List<Activite> GetAll() {
        return activiteRepository.findAll();
    }

    @Override
    public Activite Update(long id, Activite activite) {
        return activiteRepository.findById(id)
                .map(activite1 -> {
                    activite1.setNom(activite1.getNom());
                    activite1.setDateCreation(activite1.getDateCreation());
                    activite1.setDateDebut(activite1.getDateDebut());
                    activite1.setDateFin(activite1.getDateFin());
                    activite1.setLieu(activite1.getLieu());
                    activite1.setDescription(activite1.getDescription());
                    activite1.setImage(activite1.getImage());

                    return activiteRepository.save(activite1);
                }).orElseThrow(() -> new RuntimeException("Activite non trouvéé"));
    }

    @Override
    public String Delete(long id) {
        activiteRepository.deleteById(id);
        return "Supprimer avec succes";
    }

    @Override
    public Activite GetById(long id) {
        return activiteRepository.findById(id).get();
    }

    @Override
    public Activite FindAllAct() {
        return (Activite) activiteRepository.findAll();
    }

    @Override
    public String attribuerSalle(long idsalle, long idactivite) {
        Activite activitecourant = activiteRepository.findById(idactivite).orElse(null);
        Salle salleverifiee = salleRepository.findById(idsalle).orElse(null);
        if (salleverifiee != null) {
            activitecourant.setSalle(salleverifiee);
            activiteRepository.save(activitecourant);
            return "Salle attribuée avec succès !";
        } else
            return "Cette salle n'existe pas !";
    }

    @Override
    public Activite GetByEtat(Etat etat) {
        return activiteRepository.findByEtat(etat);
    }

}
