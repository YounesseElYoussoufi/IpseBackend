package com.example.ipsebackend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Retenue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id ;
    public String Lib;
    public Double montant;
    public Date dateDebut;
    public Date dateFin;
    @ManyToOne
    private EmployeActif employeActif;
}
