package com.odc.Apiodkerp.Repository;

import com.odc.Apiodkerp.Models.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import com.odc.Apiodkerp.Models.IntervenantExterne;

public interface IntervenantExtRepo extends JpaRepository<IntervenantExterne, Long> {
    IntervenantExterne findByEmail(String email);
    //IntervenantExterne findByLoginAndPassword(String login, String password);
}
