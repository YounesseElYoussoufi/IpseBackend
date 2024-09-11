package com.example.ipsebackend.controller;

import com.example.ipsebackend.dto.EmployeAvecEtudiantsDTO;
//import com.example.ipsebackend.services.EmployeService;
import com.example.ipsebackend.service.EmployeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employes")
@CrossOrigin("*")
public class EmployeController {

    @Autowired
    private EmployeService employeService;

    @GetMapping("/matricule/{matricule}/etudiants")
    public EmployeAvecEtudiantsDTO getEmployeAvecEtudiantsByMatricule(@PathVariable String matricule) {
        return employeService.getEmployeAvecEtudiantsByMatricule(matricule);
    }


}
