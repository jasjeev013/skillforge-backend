package com.skillforge.platform.services.impl;

import com.skillforge.platform.models.User;
import com.skillforge.platform.payloads.ApiResponseObject;
import com.skillforge.platform.payloads.UserDto;
import com.skillforge.platform.repositories.UserRepository;
import com.skillforge.platform.services.UserService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private ModelMapper modelMapper;
    @Override
    public ResponseEntity<ApiResponseObject> getUserDetails(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserDto userDto = modelMapper.map(user, UserDto.class);
        return new ResponseEntity<>(new ApiResponseObject("User fetched successfully",true,userDto), HttpStatus.OK);
    }
}
