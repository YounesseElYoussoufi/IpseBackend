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
public class Etudiant {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
public Long id;

public String cne ;
    public String nom  ;
public String prenom ;
public String nomComplet ;
public String aanneeDepart ;

    @ManyToOne
    @JoinColumn(name = "employe_id")
    private Employe employe;


    @ManyToMany
    @JoinTable(
            name = "etudiant_anne_scolaire",
            joinColumns = @JoinColumn(name = "etudiant_id"),
            inverseJoinColumns = @JoinColumn(name = "anne_scolaire_id")
    )
    private List<AnneScolaire> anneScolaires = new ArrayList<>();

    @OneToMany(mappedBy = "etudiant")
    private List<Note> notes = new ArrayList<>();

    @OneToMany(mappedBy = "etudiant")
    private List<Dette> dettes = new ArrayList<>();


    @OneToMany(mappedBy = "etudiant")
    private List<Paiement> paiements = new ArrayList<>();

    @OneToMany(mappedBy = "etudiant")
    private List<NiveauScolaire> niveauScolaire = new ArrayList<>();



}
