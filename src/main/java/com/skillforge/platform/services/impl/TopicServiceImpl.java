package com.skillforge.platform.services.impl;

import com.skillforge.platform.models.Course;
import com.skillforge.platform.models.Topic;
import com.skillforge.platform.payloads.ApiResponseObject;
import com.skillforge.platform.payloads.TopicDto;
import com.skillforge.platform.repositories.CourseRepository;
import com.skillforge.platform.repositories.TopicRepository;
import com.skillforge.platform.services.TopicService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TopicServiceImpl implements TopicService {
    private CourseRepository courseRepository;
    private TopicRepository topicRepository;
    private ModelMapper modelMapper;
    @Override
    public ResponseEntity<ApiResponseObject> createTopic(String courseId, TopicDto topicDto) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        Optional<Topic> prevTopic = topicRepository.findFirstByCourseIdOrderByOrderIndexDesc(courseId);
        int order = 1;
        if (prevTopic.isPresent()) order = prevTopic.get().getOrderIndex()+1;

        Topic topic = Topic.builder()
                .courseId(course.getId())
                .parentTopicId(null)
                .title(topicDto.getTitle())
                .description(topicDto.getDescription())
                .difficultyLevel(topicDto.getDifficultyLevel())
                .estimatedDurationMinutes(topicDto.getEstimatedDurationMinutes())
                .learningObjectives(topicDto.getLearningObjectives())
                .orderIndex(order)
                .build();

        Topic savedTopic= topicRepository.save(topic);

        if (prevTopic.isPresent()){
            Topic prev = prevTopic.get();
            prev.setParentTopicId(savedTopic.getId());
            topicRepository.save(prev);
        }
        return new ResponseEntity<>(new ApiResponseObject("Topic added successfully",true,modelMapper.map(savedTopic,TopicDto.class)), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<ApiResponseObject> getAllTopicsForSpecificCourse(String courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        List<Topic> topics = topicRepository.findAllByCourseIdOrderByOrderIndex(course.getId());

        List<TopicDto> topicDtos = topics.stream()
                .map(topic -> modelMapper.map(topic,TopicDto.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(new ApiResponseObject("All Topics fetched successfully",true,topicDtos),HttpStatus.OK);
    }
}
