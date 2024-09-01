package com.example.ipsebackend.dto;

import com.example.ipsebackend.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class SignupRequest {

    private String name;

    private String email;

    private String cin;



    private String password;

    private UserRole userRole;
    private String region;

}