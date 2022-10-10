package com.odc.Apiodkerp.Models;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.slf4j.helpers.Util;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FormatEmail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String libelle;

   /* @JsonIgnore
    @OneToMany(mappedBy = "formatemail")
    List<Utilisateur> utilisateursFormatMail = new ArrayList<>();*/
    @ManyToOne
    private Utilisateur utilisateur;
}
