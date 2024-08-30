package com.example.ipsebackend.dto;

import com.example.ipsebackend.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class UserDto {

        private Long id;

        private String name;

        private String email;

        private String password;

        private String cin;

        private UserRole userRole;



    }

