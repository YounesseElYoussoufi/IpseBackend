package com.example.ipsebackend.controller;

import com.example.ipsebackend.dto.EtudiantDTO;
import com.example.ipsebackend.dto.EtudiantDTOAnn;
import com.example.ipsebackend.service.EtudiantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/etudiants")
public class EtudiantController {

    @Autowired
    private EtudiantService etudiantService;

    @GetMapping("/{cne}")
    public EtudiantDTOAnn getEtudiantByCne(@PathVariable String cne) {
        return etudiantService.getEtudiantByCne(cne);
    }
}
