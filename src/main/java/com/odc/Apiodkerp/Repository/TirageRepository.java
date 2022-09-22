package com.odc.Apiodkerp.Repository;

import com.odc.Apiodkerp.Models.Tirage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TirageRepository extends JpaRepository<Tirage, Long> {
}
