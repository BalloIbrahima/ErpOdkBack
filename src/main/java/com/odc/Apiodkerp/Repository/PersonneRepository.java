package com.odc.Apiodkerp.Repository;

import com.odc.Apiodkerp.Models.Personne;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonneRepository extends JpaRepository<Personne, Long> {
    Personne findByEmail(String email);
}
