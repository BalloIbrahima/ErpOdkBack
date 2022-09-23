package com.odc.Apiodkerp.Models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import javax.persistence.JoinColumn;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@SuperBuilder
public class Postulant extends Personne {
    private String numero;

    @ManyToMany
    @JoinTable(name = "ListepostulantPostulant", joinColumns = {
            @JoinColumn(name = "id_postulant") }, inverseJoinColumns = {
                    @JoinColumn(name = "id_liste_postulant") })
    List<ListePostulant> listePostulants = new ArrayList<>();

    // public String toString() {
    // return this.getNom() + " " + this.getPrenom();
    // }

}
