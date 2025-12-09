package com.skillforge.platform.controllers;

import com.skillforge.platform.payloads.ApiResponseObject;
import com.skillforge.platform.payloads.FullInterviewAttemptDto;
import com.skillforge.platform.services.InterviewService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v0/interview")
public class InterviewController {
    private InterviewService interviewService;

    @GetMapping("/get/{enrollmentId}")
    public ResponseEntity<ApiResponseObject> getInterview(@PathVariable String enrollmentId){
       return interviewService.createOrGetFullInterview(enrollmentId);
    }

    @PostMapping("/create/attempt/{studentId}/{enrollmentId}")
    public ResponseEntity<ApiResponseObject> createInterviewAttempt(@PathVariable String studentId, @PathVariable String enrollmentId, @RequestBody FullInterviewAttemptDto fullInterviewAttemptDto){
        return interviewService.createInterviewAttempt(enrollmentId,studentId,fullInterviewAttemptDto);
    }

    @GetMapping("/get/attempt/{enrollmentId}")
    public ResponseEntity<ApiResponseObject> getInterviewAttempt(@PathVariable String enrollmentId){
        return interviewService.getInterviewAttempt(enrollmentId);
    }
}
