package com.skillforge.platform.services;

import com.skillforge.platform.payloads.ApiResponseObject;
import com.skillforge.platform.payloads.StudentProfileDto;
import com.skillforge.platform.payloads.StudentProgressDto;
import org.springframework.http.ResponseEntity;

public interface StudentProgressService {

    ResponseEntity<ApiResponseObject> updateStudentProgress(String enrollmentId, String learningMaterialId, StudentProgressDto studentProgressDto);
   void updateStudentQuizProgress(String enrollmentId, String quizId,String courseId);
    ResponseEntity<ApiResponseObject> getStudentProgress(String enrollmentId);
    int totalCompletedLearningMaterialAndQuizForSpecificCourse(String enrollmentId,String courseId);
    double checkCompleteness(String enrollmentId,String courseId);

}
