package com.odc.Apiodkerp.ServiceImplementation;

import com.odc.Apiodkerp.Models.Postulant;
import com.odc.Apiodkerp.Models.PostulantTire;
import com.odc.Apiodkerp.Models.Tirage;
import com.odc.Apiodkerp.Repository.PostulantRepository;
import com.odc.Apiodkerp.Repository.PostulantTrieRepository;
import com.odc.Apiodkerp.Repository.TirageRepository;
import com.odc.Apiodkerp.Service.TirageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class TirageServiceImpl implements TirageService {
    @Autowired
    TirageRepository tirageRepository;

    @Autowired
    PostulantTrieRepository postulantTrieRepository;

    @Autowired
    PostulantRepository postulantRepository;

    @Override
    public Tirage creer(Tirage t, List<Postulant> listeatirer, long nombre) {

        // djedje

        // List<Postulant> listepostulanttire = new ArrayList<>();
        // List<Long> random = new ArrayList<>();
        // for (Postulant p : listeatirer) {
        // random.add(p.getId());
        // }

        // Collections.shuffle(random);

        // for(int i=0; i<nombre ;i++) {
        // long j = random.get(i);
        // listepostulanttire.add(postulantRepository.findById(j).orElseThrow());
        // }

        // System.out.println(listepostulanttire);

        Tirage tirage = tirageRepository.save(t);
        // declaration de la liste qui sera retourne
        List<Postulant> listpostulant = new ArrayList<>();
        List<Long> listId = new ArrayList<>();

        List<PostulantTire> postulantTires=new ArrayList<>();

        for (Postulant p : listeatirer) {
            listId.add(p.getId());
        }

        System.out.println(listId);
        Collections.shuffle(listId);
        System.out.println(listId);

        for (int i = 1; i <= nombre; i++) {

            Long k = listId.get(i);
            listpostulant.add(postulantRepository.findById(k).get());


        }

        // ajout des postulants trie ds la table postulant trie
        for (Postulant p : listpostulant) {
            // Création dans la table postulant tiré le postulant trié
            PostulantTire postulantTrie = new PostulantTire();
            postulantTrie.setTirage(tirage);
            // atribution du postulant
            postulantTrie.setPostulant(p);
            // atribution du tirage
            postulantTires.add(postulantTrieRepository.save(postulantTrie));
        }

        return tirageRepository.save(tirage);
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
        // List<PostulantTire> listedutirage = tiragehote.getPostulanttires();
        postulantTire.setPostulant(participant);

        postulantTire.setTirage(tiragehote);
        // listedutirage.add(postulantTire);

        postulantTrieRepository.save(postulantTire);
        // tiragehote.setPostulanttires(listedutirage);
    }

    @Override
    public Iterable<Object[]> AfficherListPost(Long idlistepostulant) {
        return tirageRepository.AfficherListPost(idlistepostulant);
    }

    @Override
    public List<Tirage> tiragesValides(Boolean valide) {
        // TODO Auto-generated method stub
        return tirageRepository.findByValider(valide);
    }
}
