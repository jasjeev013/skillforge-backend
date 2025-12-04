package com.skillforge.platform.services;

import com.skillforge.platform.payloads.ApiResponseObject;
import com.skillforge.platform.payloads.StudentProfileDto;
import com.skillforge.platform.payloads.StudentProgressDto;
import org.springframework.http.ResponseEntity;

public interface StudentProgressService {

    ResponseEntity<ApiResponseObject> updateStudentProgress(String enrollmentId, String learningMaterialId, StudentProgressDto studentProgressDto);
    ResponseEntity<ApiResponseObject> getStudentProgress(String enrollmentId);
}
