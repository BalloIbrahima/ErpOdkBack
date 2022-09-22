package com.odc.Apiodkerp.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.odc.Apiodkerp.Models.Activite;
import com.odc.Apiodkerp.Models.PostulantTire;
import com.odc.Apiodkerp.Models.Presence;

public interface PresenceRepository extends JpaRepository<Presence, Long> {

    Presence findByPostulantTire(PostulantTire postulantTire);

    List<Presence> findByActivite(Activite activite);

}
