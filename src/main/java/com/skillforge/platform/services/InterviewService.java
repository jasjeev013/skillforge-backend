package com.skillforge.platform.services;

import com.skillforge.platform.payloads.ApiResponseObject;
import com.skillforge.platform.payloads.FullInterviewAttemptDto;
import com.skillforge.platform.payloads.FullInterviewDto;
import com.skillforge.platform.payloads.InterviewResponseDto;
import org.springframework.http.ResponseEntity;

public interface InterviewService {
    ResponseEntity<ApiResponseObject> createOrGetFullInterview(String enrollmentId);
    ResponseEntity<ApiResponseObject> createInterviewAttempt(String enrollmentId, String studentId, FullInterviewAttemptDto fullInterviewAttemptDto);
    ResponseEntity<ApiResponseObject> getInterviewAttempt(String enrollmentId);
    InterviewResponseDto generateAllInterviewQuestions(String enrollmentId);
}
