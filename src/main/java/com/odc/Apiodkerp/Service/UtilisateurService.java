package com.odc.Apiodkerp.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.odc.Apiodkerp.Models.Role;
import com.odc.Apiodkerp.Models.Utilisateur;

public interface UtilisateurService {

    Utilisateur creer(Utilisateur utilisateur);

    Utilisateur update(Utilisateur utilisateur);

    Utilisateur modifierRole(Utilisateur utilisateur);

    Utilisateur login(String login, String password);

    void delete(Long id);

    Utilisateur getById(Long id);

    Utilisateur getByEmail(String email);

    List<Utilisateur> getAll();

    List<Utilisateur> RetrouverParRole(Role role);

    Long TotalPersonnel();

    Long TotalEntite();

    Utilisateur trouverParLoginAndPass(String login, String password);

    List<Utilisateur> RecupererUserParEtat(Boolean bool);

}
