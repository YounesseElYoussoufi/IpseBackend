package com.example.ipsebackend.repositories;

import com.example.ipsebackend.entities.Dette;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetteRepository extends JpaRepository<Dette, Long> {
}
