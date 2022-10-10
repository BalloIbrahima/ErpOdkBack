package com.odc.Apiodkerp.ServiceImplementation;

import com.odc.Apiodkerp.Models.Role;
import com.odc.Apiodkerp.Models.Utilisateur;
import com.odc.Apiodkerp.Repository.UtilisateurRepository;
import com.odc.Apiodkerp.Service.UtilisateurService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UtilisateurServiceImpl implements UtilisateurService {

    @Autowired
    UtilisateurRepository repos;

    @Bean
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
        Utilisateur user = this.getById(utilisateur.getId());
        System.out.println(user.getPassword());

        utilisateur.setRole(user.getRole());

        if (utilisateur.getPassword() == null || utilisateur.getPassword() == "") {

            utilisateur.setPassword(user.getPassword());

        } else if (utilisateur.getPassword() != null) {
            System.out.println("non null");

            utilisateur.setPassword(passwordEncoder().encode(utilisateur.getPassword()));

        }
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
        try {
            Utilisateur utilisateur = repos.findByLogin(login);

            if (passwordEncoder().matches(password, utilisateur.getPassword())) {

                return utilisateur;
            } else
                return null;
        } catch (Exception e) {
            // TODO: handle exception
            return null;
        }

    }

    @Override
    public Utilisateur getByEmail(String email) {
        // TODO Auto-generated method stub
        return repos.findByEmail(email);
    }

    @Override
    public List<Utilisateur> RetrouverParRole(Role role) {
        // TODO Auto-generated method stub
        return repos.findByRole(role);
    }

    @Override
    public Long TotalPersonnel() {
        return repos.Total();
    }

    @Override
    public Long TotalEntite() {
        return repos.TotalEntite();
    }

    @Override
    public Utilisateur modifierRole(Utilisateur utilisateur) {
        // TODO Auto-generated method stub

        Utilisateur user = this.getById(utilisateur.getId());
        System.out.println(user.getPassword());

        utilisateur.setRole(user.getRole());

        utilisateur.setPassword(user.getPassword());

        return repos.save(utilisateur);
    }

    @Override
    public List<Utilisateur> RecupererUserParEtat(Boolean bool) {
        // TODO Auto-generated method stub
        return repos.findByActive(bool);
    }

    @Override
    public Utilisateur trouverParLoginAndPass(String login, String password) {
        // TODO Auto-generated method stub
        return repos.findByLoginAndPassword(login, password);
    }
}
