package com.example.ipsebackend.dto;
import com.example.ipsebackend.enums.CategoriieAct;
import lombok.Data;
import java.util.List;

@Data
public class EmployeActifDTO {
    private Long id;
    private String matricule;
    private String cin;
    private String nomPrenomAgent;
    private String email;
    private String contact;
    private int nbretudiant;
    private String matriculeEmp;
    private CategoriieAct categorieAct;
}