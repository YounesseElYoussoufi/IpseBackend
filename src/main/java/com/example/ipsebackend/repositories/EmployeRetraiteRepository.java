package com.example.ipsebackend.repositories;

import com.example.ipsebackend.entities.EmployeRetraite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeRetraiteRepository extends JpaRepository<EmployeRetraite, Long> {

EmployeRetraite findByNRCAR(String NRCAR);
}
