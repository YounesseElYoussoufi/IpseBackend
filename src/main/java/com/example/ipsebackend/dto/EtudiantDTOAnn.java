package com.example.ipsebackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EtudiantDTOAnn {
    private Long id;
    private String cne;
    private String nom;
    private String prenom;
    private String nomComplet;
    private String aanneeDepart;
    private List<AnneScolaireDTO> anneScolaires;
}
