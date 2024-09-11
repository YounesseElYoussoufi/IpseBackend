package com.example.ipsebackend.repositories;

import com.example.ipsebackend.entities.Dette;
import com.example.ipsebackend.entities.Paiement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaiementRepository  extends JpaRepository<Paiement, Long> {

    Paiement findByDatePaiementAndEmployeRetraiteIdAndEtudiant_Prenom(String anneeDate, Long employeRetraiteId, String etudiant_Prenom);
}
