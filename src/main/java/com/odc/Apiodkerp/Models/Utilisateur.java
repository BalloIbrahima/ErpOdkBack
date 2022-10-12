package com.odc.Apiodkerp.Models;

import java.util.ArrayList;
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
public class Utilisateur extends Personne {

    private String login;
    private String password;
    private String image;
    private Boolean active;
    private  Boolean notif;

    // relation avec tirage
    @JsonIgnore
    @OneToMany(mappedBy = "utilisateur")
    private List<Tirage> tirages = new ArrayList<>();

    // relations avec activites
    @JsonIgnore
    @OneToMany(mappedBy = "createur")
    private List<Activite> activitesCreateurs = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "leader")
    private List<Activite> activitesLeads = new ArrayList<>();

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "UtilisateurActivite", joinColumns = {
            @JoinColumn(name = "id_utilisateur")}, inverseJoinColumns = {
            @JoinColumn(name = "id_activite")})
    List<Activite> activitesFormateurs = new ArrayList<>();

    // relation avec salle
    @JsonIgnore
    @OneToMany(mappedBy = "utilisateur")
    private List<Salle> salles = new ArrayList<>();

    // relation avec rolle
    @ManyToOne
    private Role role;

    // Un utilisateur fait parti d'une entite
    @JsonIgnore
    @OneToMany(mappedBy = "activite")
    List<AouP> aoup = new ArrayList<>();


    @JsonIgnore
    @OneToMany(mappedBy = "utilisateurEntite")
    List<Entite> entites = new ArrayList<>();


    @ManyToOne
    private Entite monEntite;

    @JsonIgnore
    @OneToOne(mappedBy = "gerant", cascade = CascadeType.ALL)
    private  Entite gererEntite;


    @JsonIgnore
    @ManyToMany(mappedBy = "commissionsInterne")
    List<Tache> commissions = new ArrayList<>();


    @JsonIgnore
    @OneToMany(mappedBy = "utilisateur")
    List<FormatEmail> ListeFormatEmail  = new ArrayList<>();


    @JsonIgnore
    @OneToMany(mappedBy="porteurInterne")
    List<Tache> mestaches=new ArrayList<>();
}