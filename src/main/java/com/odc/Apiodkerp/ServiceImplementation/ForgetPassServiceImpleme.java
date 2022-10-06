package com.odc.Apiodkerp.ServiceImplementation;

import org.springframework.stereotype.Service;

import com.odc.Apiodkerp.Models.ForgetPass;
import com.odc.Apiodkerp.Repository.ForgetPassRepo;
import com.odc.Apiodkerp.Service.ForgetPassService;

@Service
public class ForgetPassServiceImpleme implements ForgetPassService {

    ForgetPassRepo forgetRepo;

    @Override
    public ForgetPass Create(ForgetPass forgetPass) {
        return forgetRepo.save(forgetPass);
    }
}
