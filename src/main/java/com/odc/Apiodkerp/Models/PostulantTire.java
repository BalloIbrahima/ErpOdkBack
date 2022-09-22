package com.odc.Apiodkerp.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class PostulantTire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    Postulant postulant;
    @ManyToOne
    Tirage tirage;

    /*@JsonIgnore
    @OneToMany(mappedBy = "postulanttires")
    private List<Tirage> tirages = new ArrayList<>();*/

}
