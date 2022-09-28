package com.odc.Apiodkerp.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.odc.Apiodkerp.Models.Statut;

import ch.qos.logback.core.status.Status;

public interface StatusRepository extends JpaRepository<Statut, Long> {

    Statut findByLibelle(String libelle);
}
