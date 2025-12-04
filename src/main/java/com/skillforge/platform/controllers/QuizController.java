package com.skillforge.platform.controllers;

import com.skillforge.platform.payloads.ApiResponseObject;
import com.skillforge.platform.payloads.QuizDto;
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

    @PostMapping("/create/{topicId}")
    public ResponseEntity<ApiResponseObject> createQuiz(@PathVariable String topicId, @RequestBody QuizDto quizDto){
        return quizService.createQuiz(topicId, quizDto);
    }

    @GetMapping("/topic/{topicId}")
    public ResponseEntity<ApiResponseObject> getAllQuizzesForSpecificTopic(@PathVariable String topicId){
        return quizService.getQuizzesForSpecificTopic(topicId);
    }
}
