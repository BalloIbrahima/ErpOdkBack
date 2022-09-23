package com.odc.Apiodkerp.Repository;

import com.odc.Apiodkerp.Models.Etat;
import com.odc.Apiodkerp.Models.Historique;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoriqueRepo extends JpaRepository<Historique, Long> {

}
