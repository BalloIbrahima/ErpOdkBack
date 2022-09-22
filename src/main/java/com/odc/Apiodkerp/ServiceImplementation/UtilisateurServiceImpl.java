package com.odc.Apiodkerp.ServiceImplementation;

import com.odc.Apiodkerp.Models.Utilisateur;
import com.odc.Apiodkerp.Repository.UtilisateurRepository;
import com.odc.Apiodkerp.Service.UtilisateurService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UtilisateurServiceImpl implements UtilisateurService {

    @Autowired
    private UtilisateurRepository repos;

    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public Utilisateur creer(Utilisateur utilisateur) {
        // TODO Auto-generated method stub
        utilisateur.setPassword(passwordEncoder().encode(utilisateur.getPassword()));
        return repos.save(utilisateur);
    }

    @Override
    public Utilisateur update(Utilisateur utilisateur) {
        // TODO Auto-generated method stub
        return repos.save(utilisateur);
    }

    @Override
    public void delete(Long id) {
        // TODO Auto-generated method stub
        repos.deleteById(id);
    }

    @Override
    public Utilisateur getById(Long id) {
        // TODO Auto-generated method stub
        return repos.findById(id).get();
    }

    @Override
    public List<Utilisateur> getAll() {
        // TODO Auto-generated method stub
        return repos.findAll();
    }

    @Override
    public Utilisateur login(String login, String password) {
        // TODO Auto-generated method stub
        Utilisateur utilisateur = repos.findByLogin(login);
        if (passwordEncoder().matches(password, utilisateur.getPassword())) {
            return utilisateur;
        } else
            return null;
    }
}
