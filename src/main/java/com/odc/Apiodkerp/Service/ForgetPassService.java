package com.odc.Apiodkerp.Service;

import com.odc.Apiodkerp.Models.Etat;
import com.odc.Apiodkerp.Models.ForgetPass;

public interface ForgetPassService {

    ForgetPass Create(ForgetPass forgetPass);

    ForgetPass Recuperer(String code);
}
