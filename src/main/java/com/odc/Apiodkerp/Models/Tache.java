package com.odc.Apiodkerp.Models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

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
    private  String porteur;
    private  String realisateur;
    private  String lieu;



    @ManyToOne
    @JoinColumn(name = "utilisateur")
    private Utilisateur utilisateur;

    @ManyToOne
    @JoinColumn(name = "activite")
    private Activite activite;

    @ManyToOne
    @JoinColumn(name = "designation")
    private Designation designation;

    @ManyToOne
    @JoinColumn(name = "statut")
    private Statut statut;
}
