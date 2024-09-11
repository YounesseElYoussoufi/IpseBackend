package com.example.ipsebackend.service;

import com.example.ipsebackend.dto.EmployeAvecEtudiantsDTO;
import com.example.ipsebackend.dto.EtudiantDTO;
import com.example.ipsebackend.entities.Employe;
import com.example.ipsebackend.repositories.EmployeRepository;
import com.example.ipsebackend.repositories.EtudiantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeService {

    @Autowired
    private EmployeRepository employeRepository;

    @Autowired
    private EtudiantRepository etudiantRepository;

    public EmployeAvecEtudiantsDTO getEmployeAvecEtudiantsByMatricule(String matricule) {
        Employe employe = employeRepository.findByMatricule(matricule)
                .orElseThrow(() -> new RuntimeException("Employé non trouvé avec matricule : " + matricule));

        List<EtudiantDTO> etudiantsDTO = employe.getEtudiants().stream()
                .map(etudiant -> new EtudiantDTO(
                        etudiant.getId(),
                        etudiant.getCne(),
                        etudiant.getNom(),
                        etudiant.getPrenom(),
                        etudiant.getNomComplet(),
                        etudiant.getAanneeDepart()))
                .collect(Collectors.toList());

        return new EmployeAvecEtudiantsDTO(
                employe.getId(),
                employe.getMatricule(),
                employe.getNomPrénomAgent(),
                employe.getEmail(),
                employe.getContact(),
                employe.getNbretudiant(),
                employe.getType(),
                etudiantsDTO
        );
    }

    public List<EmployeAvecEtudiantsDTO> getAllEmployesAvecEtudiants() {
        return employeRepository.findAll().stream()
                .map(employe -> {
                    List<EtudiantDTO> etudiantsDTO = employe.getEtudiants().stream()
                            .map(etudiant -> new EtudiantDTO(
                                    etudiant.getId(),
                                    etudiant.getCne(),
                                    etudiant.getNom(),
                                    etudiant.getPrenom(),
                                    etudiant.getNomComplet(),
                                    etudiant.getAanneeDepart()))
                            .collect(Collectors.toList());

                    return new EmployeAvecEtudiantsDTO(
                            employe.getId(),
                            employe.getMatricule(),
                            employe.getNomPrénomAgent(),
                            employe.getEmail(),
                            employe.getContact(),
                            employe.getNbretudiant(),
                            employe.getType(),
                            etudiantsDTO
                    );
                })
                .collect(Collectors.toList());
    }



}
