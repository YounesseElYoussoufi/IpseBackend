package com.example.ipsebackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaiementDTOR {
    private Long id;
    private String dateDette;
    private String datePaiement;
    private Double montant;
    private String nrcar ; // Pour lier le paiement à un employé retraité
}