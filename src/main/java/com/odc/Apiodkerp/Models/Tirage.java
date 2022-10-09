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
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Tirage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private String libelle;
    private Date date;
    private boolean valider;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "idlistepostulant")
    ListePostulant listepostulant;

    @ManyToOne
    Utilisateur utilisateur;

    //@JsonIgnore
    @OneToMany(mappedBy = "tirage")
    List<PostulantTire> postulanttires = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "activite_id", referencedColumnName = "id")
    private Activite activite;

}
