package com.odc.Apiodkerp.Repository;

import com.odc.Apiodkerp.Models.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {

    Utilisateur findByLogin(String login);

    Utilisateur findByEmail(String email);
}
