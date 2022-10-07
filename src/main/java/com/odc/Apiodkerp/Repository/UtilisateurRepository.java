package com.odc.Apiodkerp.Repository;

import com.odc.Apiodkerp.Models.Role;
import com.odc.Apiodkerp.Models.Utilisateur;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {

    Utilisateur findByLogin(String login);

    Utilisateur findByLoginAndPassword(String login, String password);

    Utilisateur findByEmail(String email);

    List<Utilisateur> findByRole(Role role);

    List<Utilisateur> findByActive(Boolean active);

    @Query(value = "SELECT COUNT(utilisateur.id) FROM utilisateur", nativeQuery = true)
    public Long Total();

    @Query(value = "SELECT COUNT(entite.id) FROM entite", nativeQuery = true)
    public Long TotalEntite();
}
