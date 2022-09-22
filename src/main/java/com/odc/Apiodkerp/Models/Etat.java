package com.odc.Apiodkerp.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "etat")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Etat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String statut;



    @OneToMany
    @JoinColumn(name = "activite_id")
    @JsonIgnore
    private Activite activite;

}
