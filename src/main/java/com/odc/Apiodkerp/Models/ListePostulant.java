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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ListePostulant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private String libelle;
    private Date dateimport;

    @OneToMany(mappedBy = "listepostulant", cascade = CascadeType.ALL)
    List<Tirage> tirages = new ArrayList<>();

    @ManyToMany(mappedBy = "listePostulants", cascade = CascadeType.ALL)
    private List<Postulant> postulants = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "idactivite")
    private Activite activite;
}
