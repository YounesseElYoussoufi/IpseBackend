package com.example.ipsebackend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Paiement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id ;
    public String dateDette ;
    public String datePaiement ;
    public  Double montant ;

    @ManyToOne
    private EmployeRetraite employeRetraite;
    @ManyToOne
    @JoinColumn(name = "etudiant_id")
    private Etudiant etudiant;
}
