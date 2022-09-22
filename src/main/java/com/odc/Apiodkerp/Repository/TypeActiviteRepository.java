package com.odc.Apiodkerp.Repository;

import com.odc.Apiodkerp.Models.TypeActivite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeActiviteRepository extends JpaRepository<TypeActivite, Long> {

    TypeActivite findByLibelle(String libelle);
}
