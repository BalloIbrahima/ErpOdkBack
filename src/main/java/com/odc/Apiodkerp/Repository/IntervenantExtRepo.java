package com.odc.Apiodkerp.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.odc.Apiodkerp.Models.IntervenantExterne;

public interface IntervenantExtRepo extends JpaRepository<IntervenantExterne, Long> {
    IntervenantExterne findByEmail(String email);
}
