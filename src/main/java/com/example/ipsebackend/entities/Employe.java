package com.example.ipsebackend.entities;

import com.example.ipsebackend.enums.Categorie;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String NomPrénomAgent;

    private String email;
    private String contact;
    private int nbretudiant;

    @Column(name = "type", insertable = false, updatable = false)
    private String type;

}