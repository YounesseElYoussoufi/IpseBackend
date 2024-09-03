package com.example.ipsebackend.repositories;

import com.example.ipsebackend.entities.EmployeActif;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeActifRepository extends JpaRepository<EmployeActif,Long> {

   EmployeActif findByMatriculeEmp(String matricul);
}
