package com.odc.Apiodkerp.Repository;

import java.util.List;

import com.odc.Apiodkerp.Models.AouP;
import org.springframework.data.jpa.repository.JpaRepository;

import com.odc.Apiodkerp.Models.Activite;
import com.odc.Apiodkerp.Models.PostulantTire;
import com.odc.Apiodkerp.Models.Presence;
import org.springframework.stereotype.Repository;

@Repository
public interface PresenceRepository extends JpaRepository<Presence, Long> {

    Presence findByAouP(AouP aouP);

    List<Presence> findByActivite(Activite activite);

}
