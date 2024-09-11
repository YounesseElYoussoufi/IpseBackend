package com.example.ipsebackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecuPaiementResponseDTO {
    private Long id;                // ID du reçu de paiement
    private LocalDate datePaiement; // Date du paiement
    private String nomPrenomParent; // Nom et prénom du parent
    private String nomPrenomEnfant; // Nom et prénom de l'enfant
    private String etablissement;   // Établissement scolaire
    private String classe;          // Classe de l'enfant
    private Double montant;         // Montant du paiement
    private String mois;            // Mois du paiement
}