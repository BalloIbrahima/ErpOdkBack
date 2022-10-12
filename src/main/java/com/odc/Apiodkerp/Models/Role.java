package com.odc.Apiodkerp.Models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String libellerole;

    @JsonIgnore
    @OneToMany(mappedBy = "role")
    List<Utilisateur> utilisateurs = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "RoleDroit", joinColumns = {
            @JoinColumn(name = "id_role") }, inverseJoinColumns = {
                    @JoinColumn(name = "id_droit") })
    List<Droit> droits = new ArrayList<>();
}
