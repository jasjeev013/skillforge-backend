package com.skillforge.platform.services;

import com.skillforge.platform.payloads.ApiResponseObject;
import com.skillforge.platform.payloads.TopicDto;
import org.springframework.http.ResponseEntity;

public interface TopicService {

    ResponseEntity<ApiResponseObject> createTopic(String courseId, TopicDto topicDto);
    ResponseEntity<ApiResponseObject> getAllTopicsForSpecificCourse(String courseId);
}
