package com.example.ipsebackend.repositories;

import com.example.ipsebackend.entities.EmployeActif;
import com.example.ipsebackend.enums.CategoriieAct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeActifRepository extends JpaRepository<EmployeActif,Long> {
   Page<EmployeActif> findByMatriculeContaining(String matricule, Pageable pageable);
   EmployeActif findByMatriculeEmp(String matricul);
   void deleteByMatriculeEmpIn(List<String> matriculesEmp);

  // List<EmployeActif> findByCategorie(CategoriieAct categorie);

  // List<EmployeActif> findByMatriculeEmpIn(List<String> matriculesEmp);
  void deleteByMatriculeEmp(String matriculeEmp);
 //   List<EmployeActif> findB(CategoriieAct categorie);
   // Méthode pour supprimer plusieurs employés actifs par leur matricule
   void deleteAllByMatriculeEmpIn(List<String> matriculesEmp);

    List<EmployeActif> findByCategorieAct(CategoriieAct categorie);
}
