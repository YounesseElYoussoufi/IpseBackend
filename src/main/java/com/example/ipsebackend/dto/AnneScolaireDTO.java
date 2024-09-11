package com.example.ipsebackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnneScolaireDTO {
    private Long id;
    private String anneScolaire;
    private String etablissement;
    private float notefinals1;
    private float notefinals2;
    private String niveau;
    private String groupe;
}
