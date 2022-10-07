package com.odc.Apiodkerp.ServiceImplementation;

import com.odc.Apiodkerp.Models.FormatEmail;
import com.odc.Apiodkerp.Repository.FormatEmailRepos;
import com.odc.Apiodkerp.Service.FormatEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class FormatEmailServImple implements FormatEmailService {

    @Autowired
    FormatEmailRepos formatEmailRepos;
    @Override
    public FormatEmail Create(FormatEmail formatEmail) {
        return formatEmailRepos.save(formatEmail);
    }

    @Override
    public List<FormatEmail> GetAll() {
        return formatEmailRepos.findAll();
    }

    @Override
    public FormatEmail Update(long id, FormatEmail formatEmail) {
        FormatEmail format= formatEmailRepos.findById(id).orElse(null);
        return formatEmailRepos.save(format);
    }

    @Override
    public String Delete(long id) {

        formatEmailRepos.deleteById(id);
        return "Format supprimé avec succes";
    }

    @Override
    public FormatEmail GetById(long id) {
        return formatEmailRepos.findById(id).get();
    }



   
}
