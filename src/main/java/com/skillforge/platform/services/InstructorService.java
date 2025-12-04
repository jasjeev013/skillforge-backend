package com.skillforge.platform.services;

import com.skillforge.platform.payloads.ApiResponseObject;
import com.skillforge.platform.payloads.InstructorDto;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;

public interface InstructorService {

ResponseEntity<ApiResponseObject> getInstructorProfile(String userId);
    ResponseEntity<ApiResponseObject> createInstructorProfile(String userid,InstructorDto instructorDto);
    ResponseEntity<ApiResponseObject> editInstructorProfile(String instructorId,InstructorDto instructorDto);
    ResponseEntity<ApiResponseObject> deleteInstructorProfile(String instructorId);
}
