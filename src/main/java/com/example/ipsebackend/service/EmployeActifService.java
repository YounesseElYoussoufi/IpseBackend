package com.example.ipsebackend.service;

import com.example.ipsebackend.dto.EmployeActifDTO;
import com.example.ipsebackend.dto.UserDto;
import com.example.ipsebackend.entities.EmployeActif;
import com.example.ipsebackend.entities.User;
import com.example.ipsebackend.repositories.EmployeActifRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeActifService {

    @Autowired
    private EmployeActifRepository employeActifRepository;

    /*public List<EmployeActifDTO> getAllEmployeActif() {
        List<EmployeActif> employeActifs = employeActifRepository.findAll();
        return employeActifs.stream().map(this::convertToDTO).collect(Collectors.toList());
    }*/

    public Page<EmployeActifDTO> getAllEmployeActif(int page, int size, String matricule) {
        Pageable pageable = PageRequest.of(page, size);
        Page<EmployeActif> employeActifsPage;

        if (matricule != null && !matricule.isEmpty()) {
            employeActifsPage = employeActifRepository.findByMatriculeContaining(matricule, pageable);
        } else {
            employeActifsPage = employeActifRepository.findAll(pageable);
        }

        return employeActifsPage.map(this::convertToDTO);
    }

    private EmployeActifDTO convertToDTO(EmployeActif employeActif) {
        EmployeActifDTO dto = new EmployeActifDTO();
        dto.setId(employeActif.getId());
        dto.setMatricule(employeActif.getMatricule());
        dto.setCin(employeActif.getCin());
        dto.setNomPrenomAgent(employeActif.getNomPrénomAgent());
        dto.setEmail(employeActif.getEmail());
        dto.setContact(employeActif.getContact());
        dto.setNbretudiant(employeActif.getEtudiants().size());
        dto.setMatriculeEmp(employeActif.getMatriculeEmp());
        dto.setCategorieAct(employeActif.getCategorieAct());
        return dto;
    }

    @Transactional
    public void deleteEmployeActifByMatricule(String matriculeEmp) {
        employeActifRepository.deleteByMatriculeEmp(matriculeEmp);
    }

    @Transactional
    public void deleteEmployesActifsByMatricules(List<String> matriculesEmp) {
        employeActifRepository.deleteAllByMatriculeEmpIn(matriculesEmp);
    }

    public EmployeActif saveEmployeActif(EmployeActif employeActif) {
        return employeActifRepository.save(employeActif);
    }
}
