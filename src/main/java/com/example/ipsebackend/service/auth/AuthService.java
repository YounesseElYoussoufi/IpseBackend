package com.example.ipsebackend.service.auth;

import com.example.ipsebackend.dto.SignupRequest;
import com.example.ipsebackend.dto.UserDto;

public interface AuthService {

    UserDto signupUser(SignupRequest signupRequest);

    boolean hasUserWithEmail(String email);
}
