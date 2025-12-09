package com.skillforge.platform.services.impl;

import com.skillforge.platform.constants.enums.InterviewStatus;
import com.skillforge.platform.models.*;
import com.skillforge.platform.payloads.*;
import com.skillforge.platform.repositories.*;
import com.skillforge.platform.services.AIService;
import com.skillforge.platform.services.InterviewService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class InterviewServiceImpl implements InterviewService {
    private InterviewRepository interviewRepository;
    private InterviewQuestionRepository interviewQuestionRepository;
    private ModelMapper modelMapper;
    private StudentEnrollmentRepository studentEnrollmentRepository;
    private CourseRepository courseRepository;
    private TopicRepository topicRepository;
    private AIService aiService;
    private InterviewAttemptRepository interviewAttemptRepository;
    private InterviewAnswerRepository interviewAnswerRepository;

    @Override
    public ResponseEntity<ApiResponseObject> createOrGetFullInterview(String enrollmentId) {
        Optional<Interview> interview = interviewRepository.findByEnrollmentId(enrollmentId);
        FullInterviewDto fullInterviewDto = new FullInterviewDto();
        if (interview.isPresent()){
            Interview interview1 = interview.get();
            fullInterviewDto.setInterviewDto(modelMapper.map(interview1, InterviewDto.class));

            List<InterviewQuestion> interviewQuestions =interviewQuestionRepository.findAllByInterviewId(interview1.getId());
            fullInterviewDto.setInterviewQuestionDtos(interviewQuestions.stream()
                    .map(interviewQuestion -> modelMapper.map(interviewQuestion, InterviewQuestionDto.class))
                    .toArray(InterviewQuestionDto[]::new));

        }else {
            InterviewResponseDto interviewResponseDto = generateAllInterviewQuestions(enrollmentId); // Here the generative response will come
            Interview interview1 = Interview.builder()
                    .title(interviewResponseDto.getInterviewTitle())
                    .description(interviewResponseDto.getInterviewDescription())
                    .enrollmentId(enrollmentId)
                    .isActive(true)
                    .totalQuestions(interviewResponseDto.getInterviewQuestionDtos().length)
                    .build();

            Interview savedInterview = interviewRepository.save(interview1);
            fullInterviewDto.setInterviewDto(modelMapper.map(savedInterview, InterviewDto.class));
            List<InterviewQuestionDto> interviewQuestionDtos = new ArrayList<>();
            for (AIInterviewQuestionDto aiInterviewQuestionDto:interviewResponseDto.getInterviewQuestionDtos()){
                InterviewQuestion interviewQuestion = InterviewQuestion.builder()
                        .interviewId(savedInterview.getId())
                        .questionText(aiInterviewQuestionDto.getQuestionText())
                        .orderIndex(aiInterviewQuestionDto.getOrderIndex())
                        .build();

                InterviewQuestion savedinterviewQuestion = interviewQuestionRepository.save(interviewQuestion);
                interviewQuestionDtos.add(modelMapper.map(savedinterviewQuestion, InterviewQuestionDto.class));
            }
            fullInterviewDto.setInterviewQuestionDtos(interviewQuestionDtos.toArray(new InterviewQuestionDto[0]));

        }
        return new ResponseEntity<>(new ApiResponseObject("Full Interview Material Fetched",true,fullInterviewDto), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ApiResponseObject> createInterviewAttempt(String enrollmentId,String studentId, FullInterviewAttemptDto fullInterviewAttemptDto) {
        Interview interview = interviewRepository.findByEnrollmentId(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Interview Not found"));

        List<InterviewQuestion> interviewQuestions = interviewQuestionRepository.findAllByInterviewId(interview.getId());

        FullInterviewAttemptDto ans = new FullInterviewAttemptDto();


        InterviewAttemptResponseDto interviewAttemptResponseDto = validateAllResponses(fullInterviewAttemptDto,interviewQuestions.toArray(new InterviewQuestion[0]));//  here gemini function will come
        InterviewAttempt interviewAttempt = InterviewAttempt.builder()
                .studentId(studentId)
                .interviewId(interview.getId())
                .aiFeedbackSummary(interviewAttemptResponseDto.getAiFeedbackSummary())
                .aiOverallScore(interviewAttemptResponseDto.getAiOverallScore())
                .aiSkillEvaluation(interviewAttemptResponseDto.getAiSkillEvaluation())
                .status(InterviewStatus.EVALUATED)
                .build();

        InterviewAttempt savedInterview = interviewAttemptRepository.save(interviewAttempt);
        ans.setInterviewAttemptDto(modelMapper.map(savedInterview,InterviewAttemptDto.class));

        List<InterviewAnswerDto> interviewAnswerDtos = new ArrayList<>();
        for (InterviewAnswerDto interviewAnswerDto: interviewAttemptResponseDto.getInterviewAnswerDtos()){
            InterviewAnswer interviewAnswer = modelMapper.map(interviewAnswerDto,InterviewAnswer.class);
            interviewAnswer.setInterviewAttemptId(savedInterview.getId());

            InterviewAnswer savedInterviewAnswer =  interviewAnswerRepository.save(interviewAnswer);
           interviewAnswerDtos.add(modelMapper.map(savedInterviewAnswer,InterviewAnswerDto.class));

        }

        ans.setInterviewAnswerDto(interviewAnswerDtos.toArray(new InterviewAnswerDto[0]));
        return new ResponseEntity<>(new ApiResponseObject("Interview Attempt Saved",true,ans),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ApiResponseObject> getInterviewAttempt(String enrollmentId) {
        Interview interview = interviewRepository.findByEnrollmentId(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Interview not found"));
        InterviewAttempt interviewAttempt = interviewAttemptRepository.findByInterviewId(interview.getId())
                .orElseThrow(() -> new RuntimeException("Interview Attempt not found"));

        FullInterviewAttemptDto fullInterviewAttemptDto = new FullInterviewAttemptDto();
        fullInterviewAttemptDto.setInterviewAttemptDto(modelMapper.map(interviewAttempt,InterviewAttemptDto.class));

        List<InterviewAnswerDto> interviewAnswers = new ArrayList<>();
        for (InterviewAnswer interviewAnswer: interviewAnswerRepository.findAllByInterviewAttemptId(interviewAttempt.getId())){
            interviewAnswers.add(modelMapper.map(interviewAnswer,InterviewAnswerDto.class));
        }
        fullInterviewAttemptDto.setInterviewAnswerDto(interviewAnswers.toArray(new InterviewAnswerDto[0]));
        return new ResponseEntity<>(new ApiResponseObject("Fetched full attempt",true,fullInterviewAttemptDto),HttpStatus.OK);
    }

    @Override
    public InterviewResponseDto generateAllInterviewQuestions(String enrollmentId) {
        StudentEnrollment studentEnrollment = studentEnrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Student Enrollment not found"));

        Course course= courseRepository.findById(studentEnrollment.getCourseId()).get();
        List<String> topics = topicRepository.findAllByCourseIdOrderByOrderIndex(course.getId()).stream()
                .map(Topic::getTitle)
                .toList();
        InterviewRequestDto interviewRequestDto = InterviewRequestDto.builder()
                .courseTitle(course.getTitle())
                .topicTitles(topics.toArray(new String[0])).build();

        return aiService.generateInterviewQuestions(interviewRequestDto);
    }

    InterviewAttemptResponseDto validateAllResponses(FullInterviewAttemptDto fullInterviewAttemptDto,InterviewQuestion[] interviewQuestions){
        InterviewAttemptRequestDto interviewRequestDto = new InterviewAttemptRequestDto();
        List<String> questionIds = new ArrayList<>();
        List<String> questions = new ArrayList<>();
        List<String > answers = new ArrayList<>();

        for (int i = 0; i < interviewQuestions.length; i++) {
            questionIds.add(interviewQuestions[i].getId());
            questions.add(interviewQuestions[i].getQuestionText());
            answers.add(fullInterviewAttemptDto.getInterviewAnswerDto()[i].getStudentAnswer());
        }

        interviewRequestDto.setAnswers(answers.toArray(new String[0]));
        interviewRequestDto.setQuestions(questions.toArray(new String[0]));
        interviewRequestDto.setQuestionIds(questionIds.toArray(new String[0]));

        return aiService.evaluateInterviewAttempt(interviewRequestDto);

    }
}
