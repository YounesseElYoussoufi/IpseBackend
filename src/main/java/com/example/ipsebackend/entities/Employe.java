package com.example.ipsebackend.entities;

import com.example.ipsebackend.enums.Categorie;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "TYPE",length = 4)
@Data @NoArgsConstructor @AllArgsConstructor
public abstract class Employe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "Matricule_NumPension")
    private String matricule;

    private String cin;

    @Column(name = "Nom_Prenom_Agent")
    private String nomPrénomAgent; // Renommez-le en utilisant une minuscule initiale

    private String email;
    private String contact;
    private int nbretudiant;

    @Column(name = "type", insertable = false, updatable = false)
    private String type;

    @OneToMany(mappedBy = "employe", cascade = CascadeType.ALL)
    private List<Etudiant> etudiants = new ArrayList<>();

}