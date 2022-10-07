package com.odc.Apiodkerp.Repository;

import com.odc.Apiodkerp.Models.Postulant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostulantRepository extends JpaRepository<Postulant, Long> {
    Postulant findByEmail(String email);


}
