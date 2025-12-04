package com.skillforge.platform.services.impl;

import com.skillforge.platform.constants.enums.EnrollmentStatus;
import com.skillforge.platform.models.Course;
import com.skillforge.platform.models.InstructorProfile;
import com.skillforge.platform.models.StudentEnrollment;
import com.skillforge.platform.models.StudentProfile;
import com.skillforge.platform.payloads.ApiResponseObject;
import com.skillforge.platform.payloads.CourseDto;
import com.skillforge.platform.payloads.StudentEnrollmentDto;
import com.skillforge.platform.repositories.CourseRepository;
import com.skillforge.platform.repositories.StudentEnrollmentRepository;
import com.skillforge.platform.repositories.StudentProfileRepository;
import com.skillforge.platform.services.StudentEnrollmentService;
import com.skillforge.platform.services.StudentProfileService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StudentEnrollmentServiceImpl implements StudentEnrollmentService {

    private ModelMapper modelMapper;
    private StudentEnrollmentRepository studentEnrollmentRepository;
    private StudentProfileRepository studentProfileRepository;
    private CourseRepository courseRepository;
    @Override
    public ResponseEntity<ApiResponseObject> enrollStudentForCourse(String studentId, String courseId) {
        StudentProfile studentProfile = studentProfileRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student Profile not found"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        StudentEnrollment studentEnrollment = StudentEnrollment.builder()
                .studentId(studentProfile.getId())
                .courseId(course.getId())
                .build();

        StudentEnrollment savedStudentEnrollment = studentEnrollmentRepository.save(studentEnrollment);


        return new ResponseEntity<>(new ApiResponseObject("Student Enrolled Successfully",true,modelMapper.map(savedStudentEnrollment, StudentEnrollmentDto.class)), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ApiResponseObject> getAllEnrolledCoursesForSpecificStudent(String studentId) {
        StudentProfile studentProfile = studentProfileRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student Profile not found"));
        List<StudentEnrollment> studentEnrollments = studentEnrollmentRepository.findAllByStudentId(studentProfile.getId());

        List<StudentEnrollmentDto> studentEnrollmentDtoList = studentEnrollments.stream().map(studentEnrollment -> modelMapper.map(studentEnrollment,StudentEnrollmentDto.class)).collect(Collectors.toList());

        return new ResponseEntity<>(new ApiResponseObject("Fetched all enrolled courses",true,studentEnrollmentDtoList),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ApiResponseObject> getCoursesEnrolledForStudent(String studentId) {
        StudentProfile studentProfile = studentProfileRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student Profile not found"));
        List<StudentEnrollment> studentEnrollments = studentEnrollmentRepository.findAllByStudentId(studentProfile.getId());
        List<CourseDto> courses = studentEnrollments.stream()
                .map(enroll -> courseRepository.findById(enroll.getCourseId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(course -> modelMapper.map(course, CourseDto.class))
                .collect(Collectors.toList());

        return new ResponseEntity<>(new ApiResponseObject("Fetched all courses",true,courses),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ApiResponseObject> getCompletedCoursesForStudent(String studentId) {
        StudentProfile studentProfile = studentProfileRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student Profile not found"));

        List<StudentEnrollment> studentEnrollments = studentEnrollmentRepository.findAllByStudentIdAndStatus(studentProfile.getId(), EnrollmentStatus.COMPLETED);
        List<CourseDto> courses = studentEnrollments.stream()
                .map(enroll -> courseRepository.findById(enroll.getCourseId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(course -> modelMapper.map(course, CourseDto.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(new ApiResponseObject("Fetched all completed courses",true,courses),HttpStatus.OK);
    }
}
