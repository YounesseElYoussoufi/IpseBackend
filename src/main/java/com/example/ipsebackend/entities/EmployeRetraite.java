package com.example.ipsebackend.entities;

import com.example.ipsebackend.enums.CategorieRet;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@DiscriminatorValue("ER")
@Data @NoArgsConstructor @AllArgsConstructor
public class EmployeRetraite extends Employe {

    @Column(name = "N_RCAR")
    private String NRCAR;

    @Enumerated(EnumType.STRING)
    @Column(name = "Categorie")
    private CategorieRet categorieRet;

    @Column(name = "Date_retraite")
    private Date dateretraite;

    private String statut;
    private String pension;
    private String totalDette;
    private Double totalPaiement;
    private Double reliquat;
    private String remarque;

    @OneToMany(mappedBy = "employeRetraite", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Dette> dettes = new ArrayList<>();

    @OneToMany(mappedBy = "employeRetraite", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Paiement> paiements = new ArrayList<>();
}