package com.skillforge.platform.controllers;

import com.skillforge.platform.payloads.ApiResponseObject;
import com.skillforge.platform.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v0/user")
public class UserController {
    private UserService userService;

    @GetMapping("/get")
    public ResponseEntity<ApiResponseObject> getUserDetails(Authentication authentication){
        String email = authentication.getName();
        return userService.getUserDetails(email);
    }
}
