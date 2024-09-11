package com.example.ipsebackend.repositories;

import com.example.ipsebackend.entities.Dette;
import com.example.ipsebackend.entities.NiveauScolaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NiveauScolaireRepository extends JpaRepository<NiveauScolaire, Long> {
    NiveauScolaire findByNiveauAndEtudiant_Prenom(String anneeDate, String etudiant_Prenom);


}
