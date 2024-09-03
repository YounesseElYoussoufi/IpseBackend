package com.example.ipsebackend.repositories;

import com.example.ipsebackend.entities.Employe;
import com.example.ipsebackend.entities.EmployeRetraite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeRepository extends JpaRepository<Employe,Long> {


  //  Page<Employe> findByNomPrénomAgentContaining(String name, Pageable pageable);
}
