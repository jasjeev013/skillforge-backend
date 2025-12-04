package com.skillforge.platform.controllers;

import com.skillforge.platform.payloads.ApiResponseObject;
import com.skillforge.platform.payloads.StudentProgressDto;
import com.skillforge.platform.services.StudentProgressService;
import lombok.AllArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v0/progress")
public class StudentProgressController {
    private StudentProgressService studentProgressService;

    @PostMapping("/update/{enrollId}/{learningMaterialId}")
    public ResponseEntity<ApiResponseObject> markLearningMaterialCompleted(@PathVariable String enrollId, @PathVariable String learningMaterialId, @RequestBody StudentProgressDto studentProgressDto){
        return studentProgressService.updateStudentProgress(enrollId,learningMaterialId,studentProgressDto);
    }

    @GetMapping("/get/{enrollmentId}")
    public ResponseEntity<ApiResponseObject> getStudentProgress(@PathVariable String enrollmentId){
        return studentProgressService.getStudentProgress(enrollmentId);
    }


}
