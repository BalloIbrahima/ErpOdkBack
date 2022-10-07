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
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  long id;
    @Lob
    private  String description;
    private  String titre;
    private  Date datenotif;


    @OneToOne
    @JoinColumn(name = "idactivite", referencedColumnName = "id")
    private  Activite activite;




}
