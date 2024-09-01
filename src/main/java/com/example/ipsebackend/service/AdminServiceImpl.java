package com.example.ipsebackend.service;

import com.example.ipsebackend.dto.SignupRequest;
import com.example.ipsebackend.dto.UserDto;
import com.example.ipsebackend.entities.User;
import com.example.ipsebackend.enums.UserRole;
import com.example.ipsebackend.repositories.UserRepository;
import com.example.ipsebackend.service.auth.AuthService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final AuthService authService;

    @Override
    public List<UserDto> getUsers() {
        return userRepository.findAll().stream()
                .filter(user -> user.getUserRole() == UserRole.EmpActif || user.getUserRole() ==UserRole.EmpRetraite)
                .map(User::getUserDto)
                .collect(Collectors.toList());
    }
    @Override
    public boolean toggleUserEnabled(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null || user.getUserRole() == UserRole.ADMIN) {
            return false;
        }
        user.setEnabled(!user.isEnabled()); // Toggle enabled state
        userRepository.save(user);
        return true;
    }

    @Override
    public UserDto updateUser(Long userId, UserDto updateRequest) {

        User user = userRepository.findById(userId).orElse(null);
        if (user == null || user.getUserRole() == UserRole.ADMIN) {
        return null;
    }


        if (updateRequest.getName() != null) {
        user.setName(updateRequest.getName());
    }
        if (updateRequest.getEmail() != null) {
        user.setEmail(updateRequest.getEmail());
    }
        if (updateRequest.getPassword() != null) {
        user.setPassword(new BCryptPasswordEncoder().encode(updateRequest.getPassword()));
    }
        if (updateRequest.getUserRole() != null) {
        user.setUserRole(updateRequest.getUserRole());
    }
        if (updateRequest.getCin() != null) {
        user.setCin(updateRequest.getCin());
    }
        if (updateRequest.getRegion() != null) {
            user.setRegion(updateRequest.getRegion());
        }


        User updatedUser = userRepository.save(user);
        return updatedUser.getUserDto();
}
    @Override
    public UserDto createUser(SignupRequest signupRequest) {
        return authService.signupUser(signupRequest);
    }

    @Override
    public boolean deleteUser(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null || user.getUserRole() == UserRole.ADMIN) {
            return false;
        }
        userRepository.delete(user);
        return true;
    }

    @Override
    public Page<UserDto> getUsers(Pageable pageable, String searchTerm) {
        Page<User> users = userRepository.findByNameContaining(searchTerm, pageable);
        return users.map(User::getUserDto);
    }
}