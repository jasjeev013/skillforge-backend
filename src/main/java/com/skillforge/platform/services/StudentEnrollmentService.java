package com.skillforge.platform.services;

import com.skillforge.platform.payloads.ApiResponseObject;
import org.springframework.http.ResponseEntity;

public interface StudentEnrollmentService {

    ResponseEntity<ApiResponseObject> enrollStudentForCourse(String studentId, String courseId);
    ResponseEntity<ApiResponseObject> getAllEnrolledCoursesForSpecificStudent(String studentId);
    ResponseEntity<ApiResponseObject> getCoursesEnrolledForStudent(String studentId);
    ResponseEntity<ApiResponseObject> getCompletedCoursesForStudent(String studentId);
}
