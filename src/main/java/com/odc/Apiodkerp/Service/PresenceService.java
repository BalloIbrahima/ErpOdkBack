package com.odc.Apiodkerp.Service;

import java.util.List;

import com.odc.Apiodkerp.Models.Activite;
import com.odc.Apiodkerp.Models.PostulantTire;
import com.odc.Apiodkerp.Models.Presence;

public interface PresenceService {

    Presence creer(Presence presence);

    Presence update(Presence presence);

    void delete(Long id);


    Presence getById(Long id);

    Presence getByPostulantTire(PostulantTire postulantTire);

    List<Presence> getByActivite(Activite activite);

    List<Presence> getAll();
}
