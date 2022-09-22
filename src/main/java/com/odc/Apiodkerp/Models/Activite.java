package com.odc.Apiodkerp.Models;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

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





    @ManyToOne
    @JoinColumn(name = "utilisateur")
    private  Utilisateur utilisateur;



    @ManyToOne
    @JoinColumn(name = "typeactivite")
    private  TypeActivite typeActivite;

    @ManyToOne
    @JoinColumn(name = "salle")
    private  Salle salle;



}
