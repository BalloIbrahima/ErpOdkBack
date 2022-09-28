package com.odc.Apiodkerp.Models;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity

@Getter
@Setter
public class IntervenantExterne extends Personne {

    private  String numero;

    @ManyToMany
    @JoinTable(name = "Activite", joinColumns = {
            @JoinColumn(name = "id_intervenant") }, inverseJoinColumns = {
            @JoinColumn(name = "id_activite") })
    List<Activite> intervenuDansActivite = new ArrayList<>();




}
