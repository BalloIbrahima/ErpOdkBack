package com.odc.Apiodkerp.Models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@SuperBuilder
public class Postulant extends Personne {
    private String numero;
    private Date DateNaissance;

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "ListepostulantPostulant", joinColumns = {
            @JoinColumn(name = "id_postulant") }, inverseJoinColumns = {
                    @JoinColumn(name = "id_liste_postulant") })
    List<ListePostulant> listePostulants = new ArrayList<>();


    @JsonIgnore
    @OneToMany(mappedBy = "postulant")
    List<AouP> aoup = new ArrayList<>();

    // public String toString() {
    // return this.getNom() + " " + this.getPrenom();
    // }

}
