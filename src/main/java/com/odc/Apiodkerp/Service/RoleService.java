package com.odc.Apiodkerp.Service;

import com.odc.Apiodkerp.Models.Role;

import java.util.List;

public interface RoleService {
    //Creer role
    Role create(Role role);
    // Afficher un role par son id
    Role read(long id);
    //Modifier role
    Role update(Role role,long id);
    //Effacer role
    Role delete(long id);
    //Afficher Tous les Roles
    List<Role> getAll();

    Role GetByLibelle(String libelle);
}
