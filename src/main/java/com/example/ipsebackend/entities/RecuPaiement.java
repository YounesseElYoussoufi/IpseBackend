package com.example.ipsebackend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecuPaiement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate datePaiement; // Date du paiement

    private String nomPrenomParent; // Nom et prénom du parent
    private String nomPrenomEnfant; // Nom et prénom de l'enfant
    private String etablissement;   // Établissement scolaire
    private String classe;          // Classe de l'enfant
    private Double montant;         // Montant à verser
    private String mois;
    // Mois du paiement
    @ManyToOne
    @JoinColumn(name = "etudiant_id")
    private Etudiant etudiant;
    @ManyToOne
    @JoinColumn(name = "employe_retraite_id")
    private EmployeRetraite employeRetraite;
}