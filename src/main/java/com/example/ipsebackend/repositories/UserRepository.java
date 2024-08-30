package com.example.ipsebackend.repositories;
import com.example.ipsebackend.entities.User;
import com.example.ipsebackend.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findFirstByEmail(String username);

    Optional<User> findByUserRole(UserRole userRole);

    Optional<User> findByEmail(String email);
}