package com.skillforge.platform.services.impl;

import com.skillforge.platform.models.LearningMaterial;
import com.skillforge.platform.models.StudentEnrollment;
import com.skillforge.platform.models.StudentProgress;
import com.skillforge.platform.payloads.ApiResponseObject;
import com.skillforge.platform.payloads.StudentProgressDto;
import com.skillforge.platform.repositories.LearningMaterialRepository;
import com.skillforge.platform.repositories.StudentEnrollmentRepository;
import com.skillforge.platform.repositories.StudentProgressRepository;
import com.skillforge.platform.services.StudentProgressService;
import lombok.AllArgsConstructor;
import org.apache.catalina.LifecycleState;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StudentProgressServiceImpl implements StudentProgressService {

    private ModelMapper modelMapper;
    private StudentEnrollmentRepository studentEnrollmentRepository;
    private LearningMaterialRepository learningMaterialRepository;
    private StudentProgressRepository studentProgressRepository;

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

        return new ResponseEntity<>(new ApiResponseObject("Student progress added",true,modelMapper.map(savedStudentProgress,StudentProgressDto.class)), HttpStatus.OK);
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
}
