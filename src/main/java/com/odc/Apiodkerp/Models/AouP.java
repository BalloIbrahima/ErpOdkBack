package com.odc.Apiodkerp.Models;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AouP {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private  boolean tirage;

    @ManyToOne
    @JoinColumn(name = "idactivite")
    private Activite activite;


    @ManyToOne
    @JoinColumn(name = "idpostulant")
    private Postulant postulant;

}