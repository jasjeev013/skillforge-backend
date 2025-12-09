package com.skillforge.platform.services.impl;

import com.skillforge.platform.constants.enums.AttemptStatus;
import com.skillforge.platform.models.*;
import com.skillforge.platform.payloads.*;
import com.skillforge.platform.repositories.*;
import com.skillforge.platform.services.AIService;
import com.skillforge.platform.services.QuizService;
import com.skillforge.platform.services.StudentProgressService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class QuizServiceImpl implements QuizService {
    private TopicRepository topicRepository;
    private QuizRepository quizRepository;
    private ModelMapper modelMapper;
    private QuizQuestionRepository quizQuestionRepository;
    private StudentProfileRepository studentProfileRepository;
    private QuizAttemptRepository quizAttemptRepository;
    private QuizAnswerRepository quizAnswerRepository;
    private StudentProgressService studentProgressService;
    private AIService aiService;

    @Override
    public ResponseEntity<ApiResponseObject> getQuiz(String quizId) {
        Quiz quiz = quizRepository.findById(quizId).orElseThrow(
                () -> new RuntimeException("Quiz not found"));

        FullQuizDto fullQuizDto = new FullQuizDto();
        fullQuizDto.setQuizDto(modelMapper.map(quiz,QuizDto.class));

        List<QuizQuestionDto> quizQuestionDtos = quizQuestionRepository.findAllByQuizIdOrderByOrderIndex(quizId)
                .stream()
                .map(quizQuestion -> modelMapper.map(quizQuestion,QuizQuestionDto.class))
                .toList();

        QuizQuestionDto[] questionDtos = quizQuestionDtos.toArray(new QuizQuestionDto[0]);

        fullQuizDto.setQuizQuestions(questionDtos);

        return new ResponseEntity<>(new ApiResponseObject("Fetched Full Quiz",true,fullQuizDto),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ApiResponseObject> createQuiz(String topicId,String instructorId, FullQuizDto fullQuizDto) {
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new RuntimeException("Topic not found"));

        Quiz quiz = modelMapper.map(fullQuizDto.getQuizDto(),Quiz.class);
        quiz.setTopicId(topic.getId());
        quiz.setInstructorId(instructorId);

        Quiz savedQuiz = quizRepository.save(quiz);

        for (QuizQuestionDto quizQuestionDto:fullQuizDto.getQuizQuestions()){
            QuizQuestion quizQuestion = modelMapper.map(quizQuestionDto,QuizQuestion.class);
            quizQuestion.setQuizId(savedQuiz.getId());
            quizQuestionRepository.save(quizQuestion);
        }
        return new ResponseEntity<>(new ApiResponseObject("Quiz Added successfully", true,modelMapper.map(savedQuiz,QuizDto.class)),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ApiResponseObject> getAttemptQuiz(String studentId, String quizId) {
        QuizAttempt quizAttempt = quizAttemptRepository.findByQuizIdAndStudentId(quizId,studentId)
                .orElseThrow( () -> new RuntimeException("Quiz Attempt not found"));

        FullAttemptQuizDto fullAttemptQuizDto = new FullAttemptQuizDto();
        fullAttemptQuizDto.setQuizAttemptDto(modelMapper.map(quizAttempt,QuizAttemptDto.class));
        List<String> questionIds = new ArrayList<>();
        List<QuizAnswerDto> quizAnswers = new ArrayList<>();
        for (QuizAnswer quizAnswer:quizAnswerRepository.findAllByAttemptId(quizAttempt.getId())){
            questionIds.add(quizAnswer.getQuestionId());
            quizAnswers.add(modelMapper.map(quizAnswer,QuizAnswerDto.class));
        }
        fullAttemptQuizDto.setQuestionIds(questionIds.toArray(new String[0]));
        fullAttemptQuizDto.setQuizAnswerDtos(quizAnswers.toArray(new QuizAnswerDto[0]));
        return new ResponseEntity<>(new ApiResponseObject("Fetched full attempt",true,fullAttemptQuizDto),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ApiResponseObject> attemptQuiz(String studentId,String quizId,String enrollmentId,String courseId, FullAttemptQuizDto fullAttemptQuizDto) {
        StudentProfile studentProfile = studentProfileRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        QuizAttempt quizAttempt = modelMapper.map(fullAttemptQuizDto.getQuizAttemptDto(),QuizAttempt.class);
        quizAttempt.setQuizId(quizId);
        quizAttempt.setStudentId(studentProfile.getId());
        quizAttempt.setStatus(AttemptStatus.SUBMITTED);

        QuizAttempt savedQuizAttempt = quizAttemptRepository.save(quizAttempt);

        for (int i = 0; i < fullAttemptQuizDto.getQuizAnswerDtos().length; i++) {
            QuizAnswerDto quizAnswerDto = fullAttemptQuizDto.getQuizAnswerDtos()[i];
            QuizAnswer quizAnswer = modelMapper.map(quizAnswerDto,QuizAnswer.class);
            quizAnswer.setAttemptId(savedQuizAttempt.getId());
            quizAnswer.setQuestionId(fullAttemptQuizDto.getQuestionIds()[i]);

            quizAnswerRepository.save(quizAnswer);
        }

        studentProgressService.updateStudentQuizProgress(enrollmentId, quizAttempt.getQuizId(), courseId);



        return new ResponseEntity<>(new ApiResponseObject("Quiz Attempt Noted Successfully",true,modelMapper.map(savedQuizAttempt,QuizAttemptDto.class)),HttpStatus.OK);
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

    @Override
    public ResponseEntity<ApiResponseObject> generateQuizQuestions(QuizRequestDto quizRequestDto) {
        QuizResponseDto quizResponseDto = aiService.generateAIQuiz(quizRequestDto);
        return new ResponseEntity<>(new ApiResponseObject("Generated Questions",true,quizResponseDto),HttpStatus.OK);
    }


}
