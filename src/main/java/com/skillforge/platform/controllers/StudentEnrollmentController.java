package com.skillforge.platform.controllers;

import com.skillforge.platform.payloads.ApiResponseObject;
import com.skillforge.platform.services.StudentEnrollmentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v0/enroll")
public class StudentEnrollmentController {

    private StudentEnrollmentService studentEnrollmentService;

    @PostMapping("/create/{studentId}/{courseId}")
    public ResponseEntity<ApiResponseObject> createStudentEnrollment(@PathVariable String studentId,@PathVariable String courseId){
        return studentEnrollmentService.enrollStudentForCourse(studentId,courseId);
    }

    @GetMapping("/get/{studentId}")
    public ResponseEntity<ApiResponseObject> getAllEnrolledCoursesForSpecificStudent(@PathVariable String studentId){
        return studentEnrollmentService.getAllEnrolledCoursesForSpecificStudent(studentId);
    }

    @GetMapping("/get/courses/{studentId}")
    public ResponseEntity<ApiResponseObject> getCoursesEnrolledForStudent(@PathVariable String studentId){
        return studentEnrollmentService.getCoursesEnrolledForStudent(studentId);
    }

    @GetMapping("/get/courses/completed/{studentId}")
    public ResponseEntity<ApiResponseObject> getAllCompletedCoursesForSpecificStudent(@PathVariable String studentId){
        return studentEnrollmentService.getCompletedCoursesForStudent(studentId);
    }


}
