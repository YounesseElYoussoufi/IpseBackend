package com.example.ipsebackend.dto;

import lombok.Data;

@Data
public class EtudiantDTO {
    private Long id;
    private String nom;
    private String prenom;
    private String email;
}
