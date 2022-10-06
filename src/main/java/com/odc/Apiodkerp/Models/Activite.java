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
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Activite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String nom;
    private Date dateCreation;
    private Date dateDebut;
    private Date dateFin;
    @Lob
    private String description;
    private String image;

    @JsonIgnore
    @ManyToMany(mappedBy = "activitesFormateurs")
    List<Utilisateur> utilisateurs = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "activite", cascade = CascadeType.ALL)
    private List<Tirage> tirages = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "createur")
    private Utilisateur createur;

    @ManyToOne
    @JoinColumn(name = "lead")
    private Utilisateur leader;

    @ManyToOne
    @JoinColumn(name = "typeActivite")
    private TypeActivite typeActivite;

    @ManyToOne
    @JoinColumn(name = "salle")
    private Salle salle;

    @ManyToOne
    @JoinColumn(name = "etat")
    private Etat etat;

    @JsonIgnore
    @OneToMany(mappedBy = "activite")
    List<Presence> presences = new ArrayList<>();



    @JsonIgnore
    @OneToMany(mappedBy = "activite", cascade = CascadeType.ALL)
    List<AouP> aoup = new ArrayList<>();


    @ManyToMany(mappedBy = "intervenuDansActivite")
    List<IntervenantExterne> intervenantExternes = new ArrayList<>();


    @JsonIgnore
    @OneToMany(mappedBy = "activite")
    List<Tache> tache = new ArrayList<>();


    @OneToOne()
    @JoinColumn(name = "idactivite", referencedColumnName = "id")
    private  Notification notification;

}
