package com.odc.Apiodkerp.Repository;

import com.odc.Apiodkerp.Models.Droit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DroitReposy extends JpaRepository<Droit, Long> {

    Droit findByLibelle(String libelle);
}
