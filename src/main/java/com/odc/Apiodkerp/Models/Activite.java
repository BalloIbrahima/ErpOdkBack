package com.odc.Apiodkerp.Models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "activite")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Activite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
   private long id;
    private String nom;
    private Date dateCreation ;
    private  Date dateDebut;
    private  Date dateFin;
    private  String lieu;
    private  String description;


@JsonIgnore
 @ManyToMany(mappedBy = "activitesFormateurs")
 List<Utilisateur> utilisateurs = new ArrayList<>();



   @ManyToOne
   @JoinColumn(name = "utilisateur")
   private  Utilisateur createur;


 @ManyToOne
 @JoinColumn(name = "utilisateur")
 private  Utilisateur leader;



    @ManyToOne
    @JoinColumn(name = "typeactivite")
    private  TypeActivite typeActivite;

    @ManyToOne
    @JoinColumn(name = "salle")
    private  Salle salle;


 @ManyToOne
 @JoinColumn(name = "etat")
 private  Etat etat;



}
