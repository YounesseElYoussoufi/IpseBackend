package com.example.ipsebackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetteDTO {
    private Long id;
    private String anneeDate;
    private Double montantAPayer;
}