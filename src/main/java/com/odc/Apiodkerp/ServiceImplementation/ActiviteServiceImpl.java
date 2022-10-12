package com.odc.Apiodkerp.ServiceImplementation;

import com.odc.Apiodkerp.Models.Activite;
import com.odc.Apiodkerp.Models.Entite;
import com.odc.Apiodkerp.Models.Salle;
import com.odc.Apiodkerp.Models.Statut;
import com.odc.Apiodkerp.Models.Tache;
import com.odc.Apiodkerp.Models.Etat;

import com.odc.Apiodkerp.Repository.ActiviteRepository;
import com.odc.Apiodkerp.Repository.EntiteRepository;
import com.odc.Apiodkerp.Repository.EtatRepository;
import com.odc.Apiodkerp.Repository.SalleRepository;
import com.odc.Apiodkerp.Repository.StatusRepository;
import com.odc.Apiodkerp.Repository.TacheRepository;
import com.odc.Apiodkerp.Service.ActiviteService;
import com.odc.Apiodkerp.Service.EntiteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ActiviteServiceImpl implements ActiviteService {
    @Autowired
    SalleRepository salleRepository;

    @Autowired
    ActiviteRepository activiteRepository;

    @Autowired
    EntiteRepository entiteRepository;

    @Autowired
    EtatRepository etatRepository;

    @Autowired
    StatusRepository statusRepository;

    @Autowired
    TacheRepository tacheRepository;

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
    public List<Activite> FindAllAct() {
        return activiteRepository.findAllByOrderByIdDesc();

        //return activiteRepository.findAll();
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
    public Long TotalActivite() {
        return activiteRepository.Total();
    }

    @Override
    public Long ToutActivite() {
        return activiteRepository.toutActivite();
        
    }

    public List<Activite> ToutActivit() {
        return activiteRepository.findAll();
        //return activiteRepository.findAllByOrderByIdAsc();

    }
    

    @Override
    public Activite GetByEtat(Etat etat) {
        return activiteRepository.findByEtat(etat);
    }

    @Override
    public List<Activite> Avenir() {
        // TODO Auto-generated method stub
        List<Activite> all = activiteRepository.findAll();

        List<Activite> avenirs = new ArrayList<>();
        Date today = new Date();

        for (Activite activite : all) {
            try {
                if (today.before(activite.getDateDebut())) {
                    avenirs.add(activite);
                }
            } catch (Exception e) {
                // TODO: handle exception
            }
            

        }

        return avenirs;
    }

    @Override
    public List<Activite> Encour() {
        // TODO Auto-generated method stub

        List<Activite> all = activiteRepository.findAll();

        List<Activite> encour = new ArrayList<>();

        Date today = new Date();
        try {
            for (Activite activite : all) {

                try {
                    if (today.after(activite.getDateDebut()) && today.before(activite.getDateFin())) {
                        encour.add(activite);
                    }
    
                } catch (Exception e) {
                    // TODO: handle exception
                }
                
            }

        } catch (Exception e) {
            // TODO: handle exception
        }

        return encour;
    }

    @Override
    public List<Activite> Termine() {
        // TODO Auto-generated method stub

        List<Activite> all = activiteRepository.findAll();

        List<Activite> termines = new ArrayList<>();

        Date today = new Date();

        for (Activite activite : all) {

            try {
                if (today.after(activite.getDateFin())) {
                    termines.add(activite);
                }
            } catch (Exception e) {
                // TODO: handle exception
            }
            

        }

        return termines;
    }

    @Override
    public List<Activite> ActiviteEntiteid(long identite) {

        Entite entite=entiteRepository.findById(identite).get();

        List<Activite> AllActivity=activiteRepository.findAll();
        List<Activite> activiteAretourner=new ArrayList<>();
        for(Activite a:AllActivity){
            try {
                if(a.getCreateur().getMonEntite().equals(entite)){
                    activiteAretourner.add(a);

                }
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
        return activiteAretourner;
        //return  activiteRepository.actEntite(identite);
    }

    @Scheduled(fixedRateString = "PT01S")
    public void etatActivite(){
        List<Activite> allActivites=activiteRepository.findAll();

        List<Tache> allTaches=tacheRepository.findAll();

        Date today = new Date();
        //
        Etat encour=etatRepository.findByStatut("EN COUR");
        Etat termine=etatRepository.findByStatut("TERMINE");
        Etat avenir=etatRepository.findByStatut("A VENIR");


        Statut encourEtat=statusRepository.findByLibelle("ENCOUR");
        Statut termineEtat=statusRepository.findByLibelle("TERMINE");
        for(Tache tache:allTaches){
            try {
                if (today.after(tache.getDatedebut()) && today.before(tache.getDatefin())) {
                    tache.setStatut(encourEtat);
                    tacheRepository.save(tache);
                
                }else if (today.after(tache.getDatefin())) {
                    tache.setStatut(termineEtat);
                    tacheRepository.save(tache);
                }

            } catch (Exception e) {
                // TODO: handle exception
            }
        }

        for(Activite activite:allActivites){
            try {
                if (today.after(activite.getDateDebut()) && today.before(activite.getDateFin())) {
                    activite.setEtat(encour);
                    activiteRepository.save(activite);
                }else if (today.before(activite.getDateDebut())) {
                    activite.setEtat(avenir);
                    activiteRepository.save(activite);
                }else if (today.after(activite.getDateFin())) {
                    activite.setEtat(termine);
                    activiteRepository.save(activite);
                }

            } catch (Exception e) {
                // TODO: handle exception
            }
        }

        //System.err.println("helllle");
        
    }

    @Override
    public List<Activite> findFiltre(String nomactivite, String typeactivite, String entite, String dtdebut, String dtfin) {
        return activiteRepository.getFiltre(nomactivite, typeactivite, entite, dtdebut, dtfin);
    }

    @Override
    public Activite RecupererParNom(String nom) {
        // TODO Auto-generated method stub
        return activiteRepository.findByNom(nom);
    }

}
