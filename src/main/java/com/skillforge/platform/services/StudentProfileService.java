package com.skillforge.platform.services;

import com.skillforge.platform.payloads.ApiResponseObject;
import com.skillforge.platform.payloads.StudentProfileDto;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;

public interface StudentProfileService {
    ResponseEntity<ApiResponseObject> createStudentProfile(String userId, StudentProfileDto studentProfileDto);
    ResponseEntity<ApiResponseObject> editStudentProfile(String studentId,StudentProfileDto studentProfileDto);
    ResponseEntity<ApiResponseObject> getStudentProfile(String userId);
}
