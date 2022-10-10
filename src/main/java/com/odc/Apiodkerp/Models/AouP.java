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
@Table(name = "Apprenant_Participant")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AouP {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private boolean tirage;

    @ManyToOne
    @JoinColumn(name = "idactivite")
    private Activite activite;

    @ManyToOne
    @JoinColumn(name = "idpostulant")
    private Postulant postulant;

    @JsonIgnore
    @OneToMany(mappedBy = "aouP", cascade = CascadeType.ALL)
    private List<Presence> presence = new ArrayList<>();

}
