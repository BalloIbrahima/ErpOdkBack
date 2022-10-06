package com.odc.Apiodkerp.ServiceImplementation;

import com.odc.Apiodkerp.Models.ForgetPass;
import com.odc.Apiodkerp.Repository.ForgetPassRepo;
import com.odc.Apiodkerp.Service.ForgetPassService;

public class ForgetPassServiceImpleme implements ForgetPassService {

    ForgetPassRepo forgetRepo;
    @Override
    public ForgetPass Create(ForgetPass forgetPass) {
        return  forgetRepo.save(forgetPass);
    }
}
