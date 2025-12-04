package com.skillforge.platform.services;

import com.skillforge.platform.payloads.ApiResponseObject;
import org.springframework.http.ResponseEntity;

public interface UserService {

    ResponseEntity<ApiResponseObject> getUserDetails(String email);
}
