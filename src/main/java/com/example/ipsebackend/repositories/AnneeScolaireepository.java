package com.example.ipsebackend.repositories;

import com.example.ipsebackend.entities.AnneScolaire;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnneeScolaireepository extends JpaRepository<AnneScolaire, Long>  {

    //AnneScolaire findByNiveauAndEtudiants

}
