package com.example.ipsebackend.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class RecuPaiementDTO {
    private String nrcar;           // NRCAR de l'employé retraité
    private Long etudiantId;        // ID de l'étudiant sélectionné
    private String etudiantNom;
    private String etablissement;   // Établissement scolaire de l'enfant
    private String classe;          // Classe de l'enfant
    private Double montant;         // Montant du paiement
    private String mois;
}