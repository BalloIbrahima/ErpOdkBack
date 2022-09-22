package com.odc.Apiodkerp.Models;

import com.odc.Apiodkerp.Enum.Genre;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@SuperBuilder
public class Entite {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
    private String libelleentite;
    private String description;
}
