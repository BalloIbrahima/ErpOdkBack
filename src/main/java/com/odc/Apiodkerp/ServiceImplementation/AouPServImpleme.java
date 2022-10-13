package com.odc.Apiodkerp.ServiceImplementation;

import com.odc.Apiodkerp.Enum.Genre;
import com.odc.Apiodkerp.Models.AouP;
import com.odc.Apiodkerp.Models.Entite;
import com.odc.Apiodkerp.Models.Presence;
import com.odc.Apiodkerp.Models.TypeActivite;
import com.odc.Apiodkerp.Repository.ActiviteRepository;
import com.odc.Apiodkerp.Repository.AouPReposy;
import com.odc.Apiodkerp.Repository.PresenceRepository;
import com.odc.Apiodkerp.Repository.TypeActiviteRepository;
import com.odc.Apiodkerp.Service.AouPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AouPServImpleme implements AouPService {

    @Autowired
    AouPReposy aouprepos;

    @Autowired
    TypeActiviteRepository typeActiviteRepository;

    @Autowired
    PresenceRepository presenceRepository;

    @Override
    public AouP Create(AouP aoup) {
        TypeActivite talk=typeActiviteRepository.findByLibelle("Talk");
        TypeActivite Evenement=typeActiviteRepository.findByLibelle("Evenement");

        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        if(aoup.getActivite().getTypeActivite().equals(talk) || aoup.getActivite().getTypeActivite().equals(Evenement)){

            try {
                Date debut=formatter.parse(formatter.format(aoup.getActivite().getDateDebut()));
                Date fin=formatter.parse(formatter.format(aoup.getActivite().getDateFin()));


                long diff = debut.getTime() - fin.getTime();
                Long nbrejour = (diff / (1000*60*60*24));


                for(int i=0; i< nbrejour;i++) {
                    Presence presence=new Presence();
                    presence.setActivite(aoup.getActivite());
                    presence.setAouP(aoup);
                    presence.setDate(new Date());
                    presence.setEtat(false);

                    presenceRepository.save(presence);
                }

            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return aouprepos.save(aoup);
    }

    @Override
    public List<AouP> GetAll() {
        return aouprepos.findAll();
    }

    @Override
    public AouP Update(long id, AouP aoup) {

        AouP aouP = aouprepos.findById(id).orElse(null);
        return aouprepos.save(aouP);
    }

    @Override
    public void Delete(long id) {
        aouprepos.deleteById(id);
        //return "Supprimer avec succes";
    }

    @Override
    public AouP GetById(long id) {
        return aouprepos.findById(id).get();
    }

    @Override
    public List<AouP> listFeminins() {
        // TODO Auto-generated method stub

        List<AouP> list = aouprepos.findAll();

        List<AouP> listArenvoyer = new ArrayList<>();

        Date now = new Date();

        for (AouP a : list) {

            if (a.getPostulant().getGenre() == Genre.Feminin) {
                listArenvoyer.add(a);
            }
        }
        return listArenvoyer;
    }

    @Override
    public List<AouP> listEnfants() {
        // TODO Auto-generated method stub
        List<AouP> list = aouprepos.findAll();

        List<AouP> listArenvoyer = new ArrayList<>();

        Date now = new Date();

        for (AouP a : list) {
            DateFormat formatter = new SimpleDateFormat("yyyyMMdd");
            int d1 = Integer.parseInt(formatter.format(a.getPostulant().getDateNaissance()));
            int d2 = Integer.parseInt(formatter.format(now));
            int age = (d2 - d1) / 10000;

            if (age < 16) {
                listArenvoyer.add(a);
            }
        }
        return listArenvoyer;
    }

    @Override
    public List<AouP> filtrerParticipant(String typeactivite, String datedebut, String datefin) {
        return aouprepos.filtrer(typeactivite, datedebut, datefin);
    }

}
