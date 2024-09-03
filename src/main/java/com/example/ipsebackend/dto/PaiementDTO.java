package com.example.ipsebackend.dto;

import lombok.Data;


import lombok.AllArgsConstructor;

import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaiementDTO {
    private Long id;
    private String dateDette;
    private String datePaiement;
    private Double montant;
}
