package com.skillforge.platform.services.impl;

import com.skillforge.platform.models.InstructorProfile;
import com.skillforge.platform.models.User;
import com.skillforge.platform.payloads.ApiResponseObject;
import com.skillforge.platform.payloads.InstructorDto;
import com.skillforge.platform.repositories.InstructorRepository;
import com.skillforge.platform.repositories.UserRepository;
import com.skillforge.platform.services.InstructorService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class InstructorServiceImpl implements InstructorService {
    private UserRepository userRepository;
    private InstructorRepository instructorRepository;
    private ModelMapper modelMapper;

    @Override
    public ResponseEntity<ApiResponseObject> getInstructorProfile(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        InstructorProfile instructorProfile =  instructorRepository.findByUserId(userId);
        return new ResponseEntity<>(new ApiResponseObject("Instructor Fetched Successfully",true,modelMapper.map(instructorProfile,InstructorDto.class)),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ApiResponseObject> createInstructorProfile(String userId,InstructorDto instructorDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        InstructorProfile instructorProfile= InstructorProfile.builder()
                .userId(user.getId())
                .hourlyRate(instructorDto.getHourlyRate())
                .qualifications(instructorDto.getQualifications())
                .expertiseDomains(instructorDto.getExpertiseDomains())
                .yearsExperience(instructorDto.getYearsExperience())
                .isVerified(true)
                .build();

        InstructorProfile savedInstructor = instructorRepository.save(instructorProfile);

        return new ResponseEntity<>(new ApiResponseObject("Instructor profile Created Successfully",true,modelMapper.map(savedInstructor,InstructorDto.class)), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<ApiResponseObject> editInstructorProfile(String instructorId, InstructorDto instructorDto) {
        InstructorProfile instructorProfile = instructorRepository.findById(instructorId)
                .orElseThrow(() -> new RuntimeException("Instructor Profile not found"));


        instructorProfile.setHourlyRate(instructorDto.getHourlyRate());
        instructorProfile.setQualifications(instructorDto.getQualifications());
        instructorProfile.setExpertiseDomains(instructorDto.getExpertiseDomains());
        instructorProfile.setYearsExperience(instructorDto.getYearsExperience());


        InstructorProfile savedInstructor = instructorRepository.save(instructorProfile);

        return new ResponseEntity<>(new ApiResponseObject("Instructor profile modified successfully",true,modelMapper.map(savedInstructor,InstructorDto.class)), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<ApiResponseObject> deleteInstructorProfile(String instructorId) {
        return null;
    }
}
