package com.odc.Apiodkerp.Service;

import com.odc.Apiodkerp.Models.ForgetPass;
import com.odc.Apiodkerp.Models.FormatEmail;

import java.util.List;

public interface FormatEmailService {



    FormatEmail Create(FormatEmail formatEmail);

    List<FormatEmail> GetAll();

    FormatEmail Update(long id, FormatEmail formatEmail);

    String Delete(long id);

    FormatEmail GetById(long id);



}
