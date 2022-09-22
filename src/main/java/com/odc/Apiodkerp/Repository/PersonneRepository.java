package com.odc.Apiodkerp.Repository;

import com.odc.Apiodkerp.Models.Personne;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonneRepository extends JpaRepository<Personne,Long> {
}
