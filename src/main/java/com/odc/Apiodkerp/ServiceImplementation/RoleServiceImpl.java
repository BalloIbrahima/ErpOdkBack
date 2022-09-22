package com.odc.Apiodkerp.ServiceImplementation;

import com.odc.Apiodkerp.Models.Role;
import com.odc.Apiodkerp.Repository.RoleRepository;
import com.odc.Apiodkerp.Service.RoleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private RoleRepository roleRepository;
    @Override
    public Role create(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public Role read(String libelle) {
        return roleRepository.findByLibellerole(libelle);
    }

    @Override
    public Role update(Role role, long id) {
        return roleRepository.save(role);
    }

    @Override
    public void delete(long id) {
        roleRepository.deleteById(id);
    }

    @Override
    public List<Role> getAll() {
        return roleRepository.findAll();
    }

}
