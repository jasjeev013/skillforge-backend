package com.skillforge.platform.services.impl;

import com.skillforge.platform.models.*;
import com.skillforge.platform.payloads.ApiResponseObject;
import com.skillforge.platform.payloads.CourseDto;
import com.skillforge.platform.payloads.LearningMaterialDto;
import com.skillforge.platform.payloads.TopicDto;
import com.skillforge.platform.repositories.*;
import com.skillforge.platform.services.AWSS3Service;
import com.skillforge.platform.services.CourseService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CourseServiceImpl implements CourseService {

    private InstructorRepository instructorRepository;
    private CourseRepository courseRepository;
    private TopicRepository topicRepository;
    private LearningMaterialRepository learningMaterialRepository;
    private QuizRepository quizRepository;
    private ModelMapper modelMapper;
    private AWSS3Service awss3Service;

    @Override
    public ResponseEntity<ApiResponseObject> createCourse(String instructorId, CourseDto courseDto) {

        InstructorProfile instructorProfile = instructorRepository.findById(instructorId)
                .orElseThrow(() -> new RuntimeException("Instructor Profile not found"));

        Course course = Course.builder()
                .instructorId(instructorProfile.getId())
                .title(courseDto.getTitle())
                .description(courseDto.getDescription())
                .difficultyLevel(courseDto.getDifficultyLevel())
                .estimatedDurationHours(courseDto.getEstimatedDurationHours())
                .learningObjectives(courseDto.getLearningObjectives())
                .price(courseDto.getPrice())
                .prerequisites(courseDto.getPrerequisites())
                .build();

        Course savedCourse = courseRepository.save(course);

        return new ResponseEntity<>(new ApiResponseObject("Course added successfully",true,modelMapper.map(savedCourse,CourseDto.class)), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<ApiResponseObject> publishCourse(String courseId) {
        Course course= courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        course.setPublished(true);
        courseRepository.save(course);
        return new ResponseEntity<>(new ApiResponseObject("Course Published Successfully",true,null),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ApiResponseObject> getAllPublishedCoursesForSpecificInstructor(String instructorId) {
        InstructorProfile instructorProfile = instructorRepository.findById(instructorId)
                .orElseThrow(() -> new RuntimeException("Instructor Profile not found"));

        List<CourseDto> courseDtos = courseRepository.findAllByInstructorIdAndPublished(instructorProfile.getId(),true)
                .stream().map(course -> modelMapper.map(course,CourseDto.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(new ApiResponseObject("Fetched all courses",true,courseDtos),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ApiResponseObject> getAllDraftCoursesForSpecificInstructor(String instructorId) {
        InstructorProfile instructorProfile = instructorRepository.findById(instructorId)
                .orElseThrow(() -> new RuntimeException("Instructor Profile not found"));

        List<CourseDto> courseDtos = courseRepository.findAllByInstructorIdAndPublished(instructorProfile.getId(),false)
                .stream().map(course -> modelMapper.map(course,CourseDto.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(new ApiResponseObject("Fetched all courses",true,courseDtos),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ApiResponseObject> addThumbnailPhoto(String courseId, MultipartFile multipartFile) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        String url;
        try{
            url = awss3Service.uploadImage(multipartFile);
        }catch (IOException e){
            throw new RuntimeException("Error adding Image");
        }

        course.setThumbnailUrl(url);
        courseRepository.save(course);
        return new ResponseEntity<>(new ApiResponseObject("Added thumbnail image",true,url),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ApiResponseObject> getAllCourses() {
        List<Course> courses = courseRepository.findAll();
        List<CourseDto> courseDtos = courses.stream().map(
                course -> modelMapper.map(course,CourseDto.class)
        ).collect(Collectors.toList());

        return new ResponseEntity<>(new ApiResponseObject("All Courses loaded successfully",true,courseDtos),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ApiResponseObject> getAllPublishedCourses() {

        List<CourseDto> courseDtos = courseRepository.findAllByPublished(true)
                .stream().map(course -> modelMapper.map(course,CourseDto.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(new ApiResponseObject("Fetched all courses",true,courseDtos),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ApiResponseObject> getSpecificCourse(String courseId) {
        Course course= courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        return new ResponseEntity<>(new ApiResponseObject("Fetched Full Course",true,course),HttpStatus.OK);
    }
    @Override
    public ResponseEntity<ApiResponseObject> getSpecificFullCourse(String courseId) {
        Course course= courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        Map<String,Object> fullCourse = new HashMap<>();

        List<Topic> topics = topicRepository.findAllByCourseIdOrderByOrderIndex(courseId);
        List<Object> fullTopics = new ArrayList<>();
        for (Topic top:topics){
            HashMap<String,Object> topic = new HashMap<>();
            List<LearningMaterial> learningMaterials = learningMaterialRepository.findAllByTopicIdOrderByOrderIndex(top.getId());
            List<Quiz> quizzes = quizRepository.findAllByTopicIdOrderByCreatedAt(top.getId());
            topic.put("topic",modelMapper.map(top, TopicDto.class));
            topic.put("learningMaterial",learningMaterials.stream().map(
                    learningMaterial -> modelMapper.map(learningMaterial, LearningMaterialDto.class)
            ).collect(Collectors.toList()));
            topic.put("quizzes",quizzes.stream().map(
                    quiz -> modelMapper.map(quiz,Quiz.class)
            ).collect(Collectors.toList()));

            fullTopics.add(topic);
        }
        fullCourse.put("course",modelMapper.map(course,CourseDto.class));
        fullCourse.put("fullTopics",fullTopics);
        return new ResponseEntity<>(new ApiResponseObject("Fetched Full Course",true,fullCourse),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ApiResponseObject> getAllCoursesForSpecificInstructor(String instructorId) {

        List<Course> courses = courseRepository.findAllByInstructorId(instructorId);
        List<CourseDto> courseDtos = courses.stream().map(
                course -> modelMapper.map(course,CourseDto.class)
        ).collect(Collectors.toList());

        return new ResponseEntity<>(new ApiResponseObject("All Courses loaded successfully",true,courseDtos),HttpStatus.OK);
    }

    @Override
    public int getTotalCountOfLearningMaterialAndQuizzes(String courseId) {
        Course course= courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        int totalCount = 0;
        List<Topic> topics = topicRepository.findAllByCourseIdOrderByOrderIndex(course.getId());

        for (Topic top:topics){
            int learningMaterials = learningMaterialRepository.countAllByTopicId(top.getId());
            int quizzes = quizRepository.countAllByTopicId(top.getId());
           totalCount+= learningMaterials+quizzes;
        }

        return totalCount;
    }
}
