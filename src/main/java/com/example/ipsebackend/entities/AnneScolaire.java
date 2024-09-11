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

    @ManyToMany
    @JoinTable(
            name = "anne_scolaire_matiere",
            joinColumns = @JoinColumn(name = "anne_scolaire_id"),
            inverseJoinColumns = @JoinColumn(name = "matiere_id")
    )
    private List<Matiere> matieres = new ArrayList<>();

    @OneToMany(mappedBy = "anneScolaire")
    private List<Note> notes = new ArrayList<>();


}
