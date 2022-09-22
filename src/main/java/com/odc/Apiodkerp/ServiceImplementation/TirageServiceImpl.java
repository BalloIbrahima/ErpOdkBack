package com.odc.Apiodkerp.ServiceImplementation;

import com.odc.Apiodkerp.Models.Postulant;
import com.odc.Apiodkerp.Models.PostulantTire;
import com.odc.Apiodkerp.Models.Tirage;
import com.odc.Apiodkerp.Repository.PostulantTrieRepository;
import com.odc.Apiodkerp.Repository.TirageRepository;
import com.odc.Apiodkerp.Service.TirageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class TirageServiceImpl implements TirageService {
    @Autowired
    TirageRepository tirageRepository;
    @Autowired
    PostulantTrieRepository postulantTrieRepository;
    @Override
    public List<Postulant> creer(Tirage tirage, List<Postulant> listeatirer, long nombre) {
        List<Postulant> listetiree = new ArrayList<>();
        PostulantTire postulantTire = new PostulantTire();
        Random ran = new Random();
        for (Postulant choisi:
             listeatirer) {
            Integer nombrealeatoire = ran.nextInt(listeatirer.size());
            listetiree.add(listeatirer.get(nombrealeatoire));
            postulantTire.setPostulant(listeatirer.get(nombrealeatoire));
            listeatirer.remove(listeatirer.get(nombrealeatoire));
        }
        tirage.setDate(new Date());
        postulantTrieRepository.save(postulantTire);
        return listetiree;
    }

    @Override
    public Tirage update(long id, Tirage tirage) {
        Tirage tirageamodifier = tirageRepository.findById(id).orElse(null);
        tirageamodifier.setLibelle(tirage.getLibelle());
        return tirageRepository.save(tirageamodifier);
    }

    @Override
    public void delete(long id) {
        tirageRepository.deleteById(id);
    }

    @Override
    public List<Tirage> getAll() {
        return tirageRepository.findAll();
    }

    @Override
    public Tirage getById(long id) {
        return tirageRepository.findById(id).get();
    }

    @Override
    public Tirage findByLibelle(String libelle) {
        return tirageRepository.findByLibelle(libelle);
    }

    @Override
    public void ajouterParticipant(Postulant participant, long idtirage) {
        Tirage tiragehote = tirageRepository.findById(idtirage).orElse(null);
        PostulantTire postulantTire = new PostulantTire();
        List<PostulantTire> listedutirage = tiragehote.getPostulanttires();
        postulantTire.setPostulant(participant);
        postulantTire.setTirage(tiragehote);
        listedutirage.add(postulantTire);
        postulantTrieRepository.save(postulantTire);
        tiragehote.setPostulanttires(listedutirage);
    }
}
