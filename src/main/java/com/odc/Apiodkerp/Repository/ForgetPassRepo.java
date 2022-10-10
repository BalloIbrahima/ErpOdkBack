package com.odc.Apiodkerp.Repository;

import com.odc.Apiodkerp.Models.Etat;
import com.odc.Apiodkerp.Models.ForgetPass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ForgetPassRepo extends JpaRepository<ForgetPass, Long> {

    ForgetPass findByCode(String code);
}
