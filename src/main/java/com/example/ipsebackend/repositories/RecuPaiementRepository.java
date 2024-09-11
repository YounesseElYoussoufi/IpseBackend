package com.example.ipsebackend.repositories;

import com.example.ipsebackend.entities.RecuPaiement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecuPaiementRepository extends JpaRepository<RecuPaiement, Long> {
}