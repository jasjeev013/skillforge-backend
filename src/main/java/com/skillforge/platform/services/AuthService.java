package com.skillforge.platform.services;

import com.skillforge.platform.payloads.ApiResponseObject;
import com.skillforge.platform.payloads.LoginRequest;
import com.skillforge.platform.payloads.RegisterRequest;
import com.skillforge.platform.payloads.UserDto;
import org.springframework.http.ResponseEntity;

public interface AuthService {

    ResponseEntity<ApiResponseObject> createUser(RegisterRequest registerRequest);
    ResponseEntity<ApiResponseObject> login(LoginRequest loginRequest);
}
