package com.odc.Apiodkerp.ServiceImplementation;

import com.odc.Apiodkerp.Models.Postulant;
import com.odc.Apiodkerp.Repository.PostulantRepository;
import com.odc.Apiodkerp.Service.PostulantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class PostulantServiceImpl implements PostulantService {

    @Autowired
    PostulantRepository postulantRepository;
    @Override
    public Postulant creer(Postulant postulant) {
        return postulantRepository.save(postulant);
    }

    @Override
    public Postulant update(Long id, Postulant postulant) {
        return postulantRepository.findById(id)
                .map(p->{
                    p.setNumero(postulant.getNumero());
                    return postulantRepository.save(p);
                }).orElseThrow(()-> new RuntimeException("Postulant non trouvé !"));
    }

    @Override
    public List<Postulant> getAll() {
        return postulantRepository.findAll();
    }

    @Override
    public Postulant GetById(Long id) {
        return postulantRepository.findById(id).get();
    }

    @Override
    public Postulant GetByEmail(String email) {
        return postulantRepository.findByEmail(email);
    }


}
