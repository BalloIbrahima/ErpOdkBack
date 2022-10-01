package com.odc.Apiodkerp.Models;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class IntervenantExterne extends Personne {

    private  String numero;

    @ManyToMany
    @JoinTable(name = "ActiviteIntervenant", joinColumns = {
            @JoinColumn(name = "id_intervenant") }, inverseJoinColumns = {
            @JoinColumn(name = "id_activite") })
    List<Activite> intervenuDansActivite = new ArrayList<>();




}
