package com.example.ipsebackend.dto;

import com.example.ipsebackend.enums.CategoriieAct;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeActifReDTO {

    private Long id;
    private String matricule;
    private String cin;
    private String nomPrenomAgent;
    private String email;
    private String contact;
    private int nbretudiant;
    private String matriculeEmp;
    private CategoriieAct categorieAct;
    private List<RetenueDto> retenues;
}
