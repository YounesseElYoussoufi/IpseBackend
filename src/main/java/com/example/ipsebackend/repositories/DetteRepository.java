package com.example.ipsebackend.repositories;

import com.example.ipsebackend.entities.Dette;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetteRepository extends JpaRepository<Dette, Long> {
//    List<Dette> findByEmployeId(Long employeId);
Dette findByAnneeDateAndEmployeRetraiteIdAndEtudiant_Prenom(String anneeDate, Long employeRetraiteId, String etudiant_Prenom);
    //Dette findByAnneeDateAndEmployeRetraiteId(String annee, Long employeId);
}
