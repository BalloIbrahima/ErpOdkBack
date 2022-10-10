package com.odc.Apiodkerp.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Salle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long nombreplace;
    @Column(unique = true)
    private String libelle;
    private String etage;
    private Boolean disponibilite;
    @Lob
    private String description;

    @OneToMany(mappedBy = "salle")
    @JsonIgnore
    List<Activite> activite = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "utilisateur")
    private Utilisateur utilisateur;

    @JsonIgnore
    @OneToMany(mappedBy="salle")
    List<Tache> mestaches=new ArrayList<>();
}
