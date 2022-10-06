package com.odc.Apiodkerp.Service;

import java.util.List;

import com.odc.Apiodkerp.Models.IntervenantExterne;

public interface IntervenantExterneService {

    IntervenantExterne creer(IntervenantExterne intervenant);

    IntervenantExterne update(IntervenantExterne intervenant);

    void delete(Long id);

    IntervenantExterne getById(Long id);

    List<IntervenantExterne> getAll();

    IntervenantExterne getByEmail(String email);
}
