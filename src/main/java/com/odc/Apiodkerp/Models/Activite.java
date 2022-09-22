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
    private  String image;



    @JsonIgnore
     @ManyToMany(mappedBy = "activitesFormateurs")
     List<Utilisateur> utilisateurs = new ArrayList<>();


@OneToOne(mappedBy = "activite", cascade= CascadeType.ALL)
private Tirage tirage;
   @ManyToOne
   @JoinColumn(name = "createur")
   private  Utilisateur createur;


 @ManyToOne
 @JoinColumn(name = "lead")
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
