package com.skillforge.platform.services;

import com.skillforge.platform.payloads.*;

public interface AIService {
    String generateImageAndStore(String prompt) throws Exception;
    QuizResponseDto generateAIQuiz(QuizRequestDto quizRequest);
    InterviewResponseDto generateInterviewQuestions(InterviewRequestDto interviewRequest);
    InterviewAttemptResponseDto evaluateInterviewAttempt(InterviewAttemptRequestDto attemptRequest);


}

