package com.odc.Apiodkerp.ServiceImplementation;

import com.odc.Apiodkerp.Models.PostulantTire;
import com.odc.Apiodkerp.Repository.PostulantTrieRepository;
import com.odc.Apiodkerp.Service.PostulantTrieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostulantTrieServiceImpl implements PostulantTrieService {

    @Autowired
    PostulantTrieRepository postulantTrieRepository;

    @Override
    public PostulantTire create(PostulantTire postulantTire) {
        return postulantTrieRepository.save(postulantTire);
    }

    @Override
    public PostulantTire update(PostulantTire postulantTire, long id) {
        return postulantTrieRepository.save(postulantTire);
    }

    @Override
    public PostulantTire read(long id) {
        return postulantTrieRepository.findById(id).get();
    }

    @Override
    public void delete(long id) {
        postulantTrieRepository.deleteById(id);
    }

    @Override
    public List<PostulantTire> getAll() {
        return postulantTrieRepository.findAll();
    }
}
