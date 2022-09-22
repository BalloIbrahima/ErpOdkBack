package com.odc.Apiodkerp.Repository;

import com.odc.Apiodkerp.Models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByLibellerole(String libelle);
}
