package com.skillforge.platform.services;

import com.skillforge.platform.payloads.*;
import org.springframework.http.ResponseEntity;

public interface QuizService {

    ResponseEntity<ApiResponseObject> getQuiz(String quizId);
    ResponseEntity<ApiResponseObject> createQuiz(String topicId, String instructorId,FullQuizDto fullQuizDto);
    ResponseEntity<ApiResponseObject> getAttemptQuiz(String studentId,String quizId);
    ResponseEntity<ApiResponseObject> attemptQuiz(String studentId,String quizId,String enrollmentId,String courseId, FullAttemptQuizDto fullAttemptQuizDto);
    ResponseEntity<ApiResponseObject> getQuizzesForSpecificTopic(String topicId);
    ResponseEntity<ApiResponseObject> generateQuizQuestions(QuizRequestDto quizRequestDto);

}
