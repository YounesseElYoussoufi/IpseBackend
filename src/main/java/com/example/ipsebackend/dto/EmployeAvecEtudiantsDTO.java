package com.example.ipsebackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeAvecEtudiantsDTO {
    private Long id;
    private String matricule;
    private String nomPrenomAgent;
    private String email;
    private String contact;
    private int nbretudiant;
    private String type;
    private List<EtudiantDTO> etudiants;
}
