package com.odc.Apiodkerp.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Entite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String libelleentite;
    
    @Lob
    private String description;
    private String image;

    @ManyToOne
    @JoinColumn(name = "utilisateur")
    private Utilisateur createur;

    @JsonIgnore
    @OneToMany(mappedBy = "monEntite")
    List<Utilisateur> utilisateurEntite = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "idutilisateur", referencedColumnName = "id", unique = true)
    private Utilisateur gerant;
}
