package com.example.ipsebackend.service;

import com.example.ipsebackend.dto.SignupRequest;
import com.example.ipsebackend.dto.UserDto;
import com.example.ipsebackend.entities.User;
import com.example.ipsebackend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public interface AdminService {


    List<UserDto> getUsers();
    UserDto updateUser(Long userId, UserDto updateRequest);
    UserDto createUser(SignupRequest signupRequest);
    boolean deleteUser(Long userId);
    boolean toggleUserEnabled(Long userId);
    Page<UserDto> getUsers(Pageable pageable, String searchTerm);
}
