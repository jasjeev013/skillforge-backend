package com.skillforge.platform.controllers;

import com.skillforge.platform.payloads.*;
import com.skillforge.platform.services.QuizService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v0/quiz")
public class QuizController {

    private QuizService quizService;

    @GetMapping("/get/{quizId}")
    public ResponseEntity<ApiResponseObject> getQuiz(@PathVariable String quizId){
        return quizService.getQuiz(quizId);
    }
    @PostMapping("/create/{topicId}/{instructorId}")
    public ResponseEntity<ApiResponseObject> createQuiz(@PathVariable String topicId,@PathVariable String instructorId, @RequestBody FullQuizDto quizDto){
        return quizService.createQuiz(topicId,instructorId, quizDto);
    }
    @GetMapping("/attempt/get/{quizId}/{studentId}")
    public ResponseEntity<ApiResponseObject> getAttemptQuiz(@PathVariable String quizId,@PathVariable String studentId){
        return quizService.getAttemptQuiz(studentId,quizId);
    }

    @PostMapping("/attempt/{quizId}/{studentId}/{enrollmentId}/{courseId}")
    public ResponseEntity<ApiResponseObject> attemptQuiz(@PathVariable String quizId,@PathVariable String studentId, @RequestBody FullAttemptQuizDto attemptQuizDto,@PathVariable String enrollmentId,@PathVariable String courseId){
        return quizService.attemptQuiz(studentId,quizId, enrollmentId,courseId, attemptQuizDto);
    }

    @GetMapping("/topic/{topicId}")
    public ResponseEntity<ApiResponseObject> getAllQuizzesForSpecificTopic(@PathVariable String topicId){
        return quizService.getQuizzesForSpecificTopic(topicId);
    }

    @PostMapping("/generate")
    public ResponseEntity<ApiResponseObject> generateQuizQuestions(@RequestBody QuizRequestDto quizRequestDto){
        return quizService.generateQuizQuestions(quizRequestDto);
    }
}
