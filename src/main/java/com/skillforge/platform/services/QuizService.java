package com.skillforge.platform.services;

import com.skillforge.platform.payloads.ApiResponseObject;
import com.skillforge.platform.payloads.QuizDto;
import org.springframework.http.ResponseEntity;

public interface QuizService {
    ResponseEntity<ApiResponseObject> createQuiz(String topicId, QuizDto quizDto);
    ResponseEntity<ApiResponseObject> getQuizzesForSpecificTopic(String topicId);
}
