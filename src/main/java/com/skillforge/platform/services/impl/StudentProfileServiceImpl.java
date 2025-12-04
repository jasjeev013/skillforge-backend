package com.skillforge.platform.services.impl;

import com.skillforge.platform.models.StudentProfile;
import com.skillforge.platform.models.User;
import com.skillforge.platform.payloads.ApiResponseObject;
import com.skillforge.platform.payloads.StudentProfileDto;
import com.skillforge.platform.repositories.StudentProfileRepository;
import com.skillforge.platform.repositories.UserRepository;
import com.skillforge.platform.services.StudentProfileService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
@AllArgsConstructor
public class StudentProfileServiceImpl implements StudentProfileService {
    private ModelMapper modelMapper;
    private UserRepository userRepository;
    private StudentProfileRepository studentProfileRepository;
    @Override
    public ResponseEntity<ApiResponseObject> createStudentProfile(String userId, StudentProfileDto studentProfileDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        StudentProfile studentProfile = StudentProfile.builder()
                .userId(user.getId())
                .learningGoals(studentProfileDto.getLearningGoals())
                .preferredLearningStyle(studentProfileDto.getPreferredLearningStyle())
                .build();

        StudentProfile savedStudentProfile = studentProfileRepository.save(studentProfile);
        return new ResponseEntity<>(new ApiResponseObject("Student Profile Created",true,modelMapper.map(savedStudentProfile,StudentProfileDto.class)), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<ApiResponseObject> editStudentProfile(String studentId, StudentProfileDto studentProfileDto) {
        StudentProfile studentProfile = studentProfileRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        studentProfile.setLearningGoals(studentProfileDto.getLearningGoals());
        studentProfile.setPreferredLearningStyle(studentProfileDto.getPreferredLearningStyle());

        StudentProfile savedStudentProfile = studentProfileRepository.save(studentProfile);

        return new ResponseEntity<>(new ApiResponseObject("Student profile edited",true,modelMapper.map(savedStudentProfile,StudentProfile.class)), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ApiResponseObject> getStudentProfile(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Optional<StudentProfile> studentProfile = studentProfileRepository.findByUserId(user.getId());
        return studentProfile.map(profile -> new ResponseEntity<>(new ApiResponseObject("User Details found", true, profile), HttpStatus.OK)).orElseThrow(() -> new RuntimeException("No student found"));
    }
}
