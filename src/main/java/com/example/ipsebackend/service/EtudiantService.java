package com.example.ipsebackend.service;

import com.example.ipsebackend.dto.AnneScolaireDTO;
import com.example.ipsebackend.dto.EtudiantDTO;
import com.example.ipsebackend.dto.EtudiantDTOAnn;
import com.example.ipsebackend.entities.Etudiant;
import com.example.ipsebackend.repositories.EtudiantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EtudiantService {

    @Autowired
    private EtudiantRepository etudiantRepository;

    public EtudiantDTOAnn getEtudiantByCne(String cne) {
        Optional<Etudiant> etudiantOptional = etudiantRepository.findByCne(cne);
        return etudiantOptional.map(this::convertToDTO).orElse(null);
    }

    private EtudiantDTOAnn convertToDTO(Etudiant etudiant) {
        EtudiantDTOAnn dto = new EtudiantDTOAnn();
        dto.setId(etudiant.getId());
        dto.setCne(etudiant.getCne());
        dto.setNom(etudiant.getNom());
        dto.setPrenom(etudiant.getPrenom());
        dto.setNomComplet(etudiant.getNomComplet());
        dto.setAanneeDepart(etudiant.getAanneeDepart());

        // Convertir les années scolaires en DTOs
        dto.setAnneScolaires(etudiant.getAnneScolaires().stream()
                .map(anneScolaire -> new AnneScolaireDTO(
                        anneScolaire.getId(),
                        anneScolaire.getAnneScolaire(),
                        anneScolaire.getEtablissement(),
                        anneScolaire.getNotefinals1(),
                        anneScolaire.getNotefinals2(),
                        anneScolaire.getNiveau(),
                        anneScolaire.getGroupe()
                )).collect(Collectors.toList()));

        return dto;
    }
}
