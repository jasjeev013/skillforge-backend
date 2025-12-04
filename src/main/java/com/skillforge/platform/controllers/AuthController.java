package com.skillforge.platform.controllers;

import com.skillforge.platform.payloads.ApiResponseObject;
import com.skillforge.platform.payloads.LoginRequest;
import com.skillforge.platform.payloads.RegisterRequest;
import com.skillforge.platform.services.AuthService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v0/auth")
public class AuthController {
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponseObject> createUser(@RequestBody RegisterRequest registerRequest){
        return authService.createUser(registerRequest);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponseObject> loginUser(@RequestBody LoginRequest loginRequest){
        return authService.login(loginRequest);
    }

}
