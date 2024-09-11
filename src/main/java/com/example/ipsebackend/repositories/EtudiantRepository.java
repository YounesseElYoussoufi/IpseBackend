package com.example.ipsebackend.repositories;

import com.example.ipsebackend.entities.Etudiant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EtudiantRepository extends JpaRepository<Etudiant, Long> {
    Optional<Etudiant> findByCne(String cne);
    Etudiant  findByCneOrCne(String cne, String cne2);
   // Etudiant findByNom(String nom);
//Etudiant findByAnneScolairesIs
    Etudiant findByNomComplet(String nom);

}
