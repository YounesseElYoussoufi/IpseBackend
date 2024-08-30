package com.example.ipsebackend.dto;

import com.example.ipsebackend.enums.UserRole;
import lombok.Data;

@Data

public class AuthenticationResponse {

    private String jwt;
    private String name;

    private Long userId;

    private UserRole userRole;

}