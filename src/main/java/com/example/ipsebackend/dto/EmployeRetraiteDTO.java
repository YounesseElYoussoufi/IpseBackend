package com.example.ipsebackend.dto;

import com.example.ipsebackend.enums.CategorieRet;
import jakarta.persistence.Column;
import lombok.Data;

import java.util.Date;
import java.util.List;


import lombok.AllArgsConstructor;

import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeRetraiteDTO {
    private Long id;
    private String nomPrenomAgent;
    private String email;
    private String contact;
    private int nbretudiant;
    private String type;
    private String NRCAR;
    private CategorieRet categorieRet;
    private Date dateretraite;
    private String statut;
    private String pension;
    private Double totalDette;
    private Double totalPaiement;
    private Double reliquat;
    private String remarque;

    private List<DetteDTO> dettes;
    private List<PaiementDTO> paiements;
    private List<EtudiantDTO> etudiants;
}
