package com.example.ipsebackend.entities;

import com.example.ipsebackend.enums.CategoriieAct;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("EA")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeActif extends Employe{


    @Column(unique = true)
    public String matriculeEmp ;
    @Enumerated(EnumType.STRING)
    public CategoriieAct categorieAct ;
     //public Double contact ;

    @OneToMany(mappedBy = "employeActif")
    private List<Retenue> retenues = new ArrayList<>();


}
