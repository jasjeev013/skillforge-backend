package com.skillforge.platform.services.impl;

import com.skillforge.platform.models.Quiz;
import com.skillforge.platform.models.Topic;
import com.skillforge.platform.payloads.ApiResponseObject;
import com.skillforge.platform.payloads.QuizDto;
import com.skillforge.platform.repositories.QuizRepository;
import com.skillforge.platform.repositories.TopicRepository;
import com.skillforge.platform.services.QuizService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class QuizServiceImpl implements QuizService {
    private TopicRepository topicRepository;
    private QuizRepository quizRepository;
    private ModelMapper modelMapper;
    @Override
    public ResponseEntity<ApiResponseObject> createQuiz(String topicId, QuizDto quizDto) {
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new RuntimeException("Topic not found"));

        Quiz quiz = Quiz.builder()
                .topicId(topic.getId())
                .title(quizDto.getTitle())
                .description(quizDto.getDescription())
                .quizType(quizDto.getQuizType())
                .difficultyLevel(quizDto.getDifficultyLevel())
                .timeLimitMinutes(quizDto.getTimeLimitMinutes())
                .totalQuestions(quizDto.getTotalQuestions())
                .passingScore(quizDto.getPassingScore())
                .build();
        Quiz savedQuiz = quizRepository.save(quiz);
    return new ResponseEntity<>(new ApiResponseObject("Quiz Created Successfully",true,modelMapper.map(savedQuiz,QuizDto.class)), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<ApiResponseObject> getQuizzesForSpecificTopic(String topicId) {
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new RuntimeException("Topic not found"));
        List<Quiz> quizzes = quizRepository.findAllByTopicIdOrderByCreatedAt(topicId);
        List<QuizDto> quizDtos = quizzes.stream()
                .map(quiz -> modelMapper.map(quiz,QuizDto.class))
                .collect(Collectors.toList());

        return new ResponseEntity<>(new ApiResponseObject("All Quizzes fetched",true,quizDtos),HttpStatus.OK);
    }
}
