package com.skillforge.platform.controllers;

import com.skillforge.platform.payloads.ApiResponseObject;
import com.skillforge.platform.payloads.StudentProfileDto;
import com.skillforge.platform.services.StudentProfileService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v0/student")
public class StudentController {
    private StudentProfileService studentProfileService;

    @GetMapping("/get/{userId}")
    public ResponseEntity<ApiResponseObject> getStudentProfile(@PathVariable String userId){
        return studentProfileService.getStudentProfile(userId);
    }

    @PostMapping("/create/{userId}")
    public ResponseEntity<ApiResponseObject> createStudentProfile(@PathVariable String userId, @RequestBody StudentProfileDto studentProfileDto){
        return studentProfileService.createStudentProfile(userId,studentProfileDto);
    }

    @PutMapping("/edit/{studentId}")
    public ResponseEntity<ApiResponseObject> editStudentProfile(@PathVariable String studentId,@RequestBody StudentProfileDto studentProfileDto){
        return studentProfileService.editStudentProfile(studentId,studentProfileDto);
    }
}
