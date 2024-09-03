package com.example.ipsebackend.repositories;

import com.example.ipsebackend.entities.Dette;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface DetteRepository extends JpaRepository<Dette, Long> {
//    List<Dette> findByEmployeId(Long employeId);
}
