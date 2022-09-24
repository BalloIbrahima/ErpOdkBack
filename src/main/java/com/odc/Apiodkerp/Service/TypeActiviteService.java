package com.odc.Apiodkerp.Service;

import java.util.List;

import com.odc.Apiodkerp.Models.TypeActivite;

public interface TypeActiviteService {

    TypeActivite creer(TypeActivite typeActivite);

    TypeActivite update(TypeActivite typeActivite);

    String delete(Long id);

    TypeActivite getById(Long id);

    TypeActivite getByLibelle(String libelle);

    List<TypeActivite> getAll();
}
