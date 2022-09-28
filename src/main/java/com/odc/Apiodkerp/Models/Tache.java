package com.odc.Apiodkerp.Models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class Tache {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  long id;
    private Date datedebut;
    private Date datefin;
    private  String lieu;



    @ManyToOne
    @JoinColumn(name = "utilisateur")
    private Utilisateur porteur;

    @ManyToOne
    @JoinColumn(name = "activite")
    private Activite activite;

    @ManyToOne
    @JoinColumn(name = "designation")
    private Designation designation;

    @ManyToOne
    @JoinColumn(name = "statut")
    private Statut statut;


    @ManyToMany
    @JoinTable(name = "tacheCommission", joinColumns = {
            @JoinColumn(name = "id_tache") }, inverseJoinColumns = {
            @JoinColumn(name = "id_utilisateur") })
    List<Utilisateur> commissions = new ArrayList<>();

}
