package com.skillforge.platform.services.impl;

import com.skillforge.platform.config.CustomUserDetails;
import com.skillforge.platform.config.CustomUserDetailsService;
import com.skillforge.platform.models.User;
import com.skillforge.platform.payloads.ApiResponseObject;
import com.skillforge.platform.payloads.LoginRequest;
import com.skillforge.platform.payloads.LoginResponse;
import com.skillforge.platform.payloads.RegisterRequest;
import com.skillforge.platform.repositories.UserRepository;
import com.skillforge.platform.services.AuthService;
import com.skillforge.platform.utils.JwtTokenUtil;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthServiceImpl  implements AuthService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private CustomUserDetailsService customUserDetailsService;
    private JwtTokenUtil jwtTokenUtil;
    @Override
    public ResponseEntity<ApiResponseObject> createUser(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("Email already in use");
        }
        User user = User.builder()
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .role(registerRequest.getRole())
                .build();
        User savedUser = userRepository.save(user);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        registerRequest.getEmail(),
                        registerRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        CustomUserDetails userDetails = (CustomUserDetails) customUserDetailsService.loadUserByUsername(savedUser.getEmail());
        String token = jwtTokenUtil.generateToken(userDetails);

        return new ResponseEntity<>(new ApiResponseObject("User created successfully",true,new LoginResponse(token,String.valueOf(savedUser.getRole()))), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<ApiResponseObject> login(LoginRequest loginRequest) {

        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        CustomUserDetails userDetails = (CustomUserDetails) customUserDetailsService.loadUserByUsername(loginRequest.getEmail());
        String token = jwtTokenUtil.generateToken(userDetails);
//        Date expirationDate = jwtTokenUtil.extractExpiration(token);

        user = userDetails.getUser();
        return new ResponseEntity<>(new ApiResponseObject("user successfully logged in",true,new LoginResponse(token,String.valueOf(user.getRole()))),HttpStatus.OK);

    }



}
