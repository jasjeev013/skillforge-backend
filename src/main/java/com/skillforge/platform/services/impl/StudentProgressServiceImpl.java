package com.skillforge.platform.services.impl;

import com.skillforge.platform.constants.enums.UnderstandingLevel;
import com.skillforge.platform.models.*;
import com.skillforge.platform.payloads.ApiResponseObject;
import com.skillforge.platform.payloads.StudentProgressDto;
import com.skillforge.platform.repositories.*;
import com.skillforge.platform.services.CourseService;
import com.skillforge.platform.services.StudentEnrollmentService;
import com.skillforge.platform.services.StudentProgressService;
import lombok.AllArgsConstructor;
import org.apache.catalina.LifecycleState;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StudentProgressServiceImpl implements StudentProgressService {

    private ModelMapper modelMapper;
    private StudentEnrollmentRepository studentEnrollmentRepository;
    private LearningMaterialRepository learningMaterialRepository;
    private StudentProgressRepository studentProgressRepository;
    private QuizRepository quizRepository;
    private CourseRepository courseRepository;
    private CourseService courseService;
    private StudentEnrollmentService studentEnrollmentService;

    @Override
    public ResponseEntity<ApiResponseObject> updateStudentProgress(String enrollmentId, String learningMaterialId, StudentProgressDto studentProgressDto) {
        StudentEnrollment studentEnrollment = studentEnrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Student Enrollment not found"));
        LearningMaterial learningMaterial = learningMaterialRepository.findById(learningMaterialId)
                .orElseThrow(() -> new RuntimeException("Learning Material not found"));

        StudentProgress studentProgress = StudentProgress.builder()
                .enrollmentId(studentEnrollment.getId())
                .learningMaterialId(learningMaterial.getId())
                .courseId(studentProgressDto.getCourseId())
                .completed(studentProgressDto.getCompleted())
                .emotionalFeedback(studentProgressDto.getEmotionalFeedback())
                .timeSpentMinutes(studentProgressDto.getTimeSpentMinutes())
                .understandingLevel(studentProgressDto.getUnderstandingLevel())
                .build();

        StudentProgress savedStudentProgress = studentProgressRepository.save(studentProgress);
        double newPercentage = checkCompleteness(enrollmentId,studentEnrollment.getCourseId());
        studentEnrollmentService.updateCurrentProgressPercentage(enrollmentId ,newPercentage);
        return new ResponseEntity<>(new ApiResponseObject("Student progress added",true,modelMapper.map(savedStudentProgress,StudentProgressDto.class)), HttpStatus.OK);
    }

    @Override
    public void updateStudentQuizProgress(String enrollmentId, String quizId, String courseId) {
        StudentEnrollment studentEnrollment = studentEnrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Student Enrollment not found"));
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        StudentProgress studentProgress = StudentProgress.builder()
                .quizId(quiz.getId())
                .courseId(courseId)
                .enrollmentId(enrollmentId)
                .completed(true)
                .completedAt(LocalDateTime.now())
                .understandingLevel(UnderstandingLevel.GOOD)
                .build();
        studentProgressRepository.save(studentProgress);
        studentEnrollmentService.updateCurrentProgressPercentage(enrollmentId ,checkCompleteness(enrollmentId,course.getId()));

    }

    @Override
    public ResponseEntity<ApiResponseObject> getStudentProgress(String enrollmentId) {
        StudentEnrollment studentEnrollment = studentEnrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Student Enrollment not found"));

        List<String> learningMaterialIds = studentProgressRepository.findAllByEnrollmentId(studentEnrollment.getId())
                .stream().map(StudentProgress::getLearningMaterialId)
                .collect(Collectors.toList());

        return new ResponseEntity<>(new ApiResponseObject("Fetched all completed learning material",true,learningMaterialIds),HttpStatus.OK);
    }

    @Override
    public int totalCompletedLearningMaterialAndQuizForSpecificCourse(String enrollmentId,String courseId) {
        return studentProgressRepository.countAllByEnrollmentIdAndCourseId(enrollmentId,courseId);
    }

    @Override
    public double checkCompleteness(String enrollmentId,String courseId) {
        return ((double) totalCompletedLearningMaterialAndQuizForSpecificCourse(enrollmentId, courseId) /courseService.getTotalCountOfLearningMaterialAndQuizzes(courseId))*100;
    }
}
