package com.example.ipsebackend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnneScolaire {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;
    public String anneScolaire;
    public String etablissement ;
    public float  notefinals1 ;
    public float  notefinals2 ;
    public String niveau ;
    public String groupe ;


    @ManyToMany(mappedBy = "anneScolaires")
    private List<Etudiant> etudiants = new ArrayList<>();
}
