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
@Table(name = "salle")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Salle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  long id;
    private  String libelle;
    private  String etape;
    private  String disponibilite;
    private  String description;


    @OneToMany(mappedBy = "salle")
    @JsonIgnore
    List<Activite>  activite=new ArrayList<>();
}
