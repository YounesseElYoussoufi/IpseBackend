package com.example.ipsebackend.repositories;

import com.example.ipsebackend.entities.EmployeRetraite;
import com.example.ipsebackend.entities.User;
import com.example.ipsebackend.enums.CategorieRet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeRetraiteRepository extends JpaRepository<EmployeRetraite, Long> {

    EmployeRetraite findByNRCAR(String NRCAR);

    Page<EmployeRetraite> findByNRCARContaining(String search, Pageable pageable);
    // Méthode pour supprimer un employé retraité par son NRCAR
    void deleteByNRCAR(String NRCAR);

    // Méthode pour supprimer plusieurs employés retraités par leur NRCAR
    void deleteAllByNRCARIn(List<String> NRCARs);

    List<EmployeRetraite> findByCategorieRet(CategorieRet categorie);
}
