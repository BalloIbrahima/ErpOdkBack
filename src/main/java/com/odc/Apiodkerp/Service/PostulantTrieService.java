package com.odc.Apiodkerp.Service;

import com.odc.Apiodkerp.Models.PostulantTire;

import java.util.List;

public interface PostulantTrieService {
    //Creer postulant tiré
    PostulantTire create(PostulantTire postulantTire);
    //Modifier Postulant tiré existant
    PostulantTire update(PostulantTire postulantTire, long id);
    //Afficher postulant tiré par id
    PostulantTire read(long id);
    //Effacer postulant tiré par id
    void delete(long id);
    // Afficher tous les postulant tiré
    List<PostulantTire> getAll();
}
