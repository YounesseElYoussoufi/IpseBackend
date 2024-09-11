package com.example.ipsebackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RetenueDto {
    private Long id;
    private String lib;
    private Double montant;
    private Date dateDebut;
    private Date dateFin;
}
