package com.example.ipsebackend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Dette {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
private String AnneeDate;
public Double MontantAPayer ;
    @ManyToOne
    @JoinColumn(name = "employe_retraite_id")
    private EmployeRetraite employeRetraite;


}
