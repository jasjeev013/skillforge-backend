package com.skillforge.platform.controllers;


import com.skillforge.platform.payloads.ApiResponseObject;
import com.skillforge.platform.payloads.TopicDto;
import com.skillforge.platform.services.TopicService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v0/topic")
public class TopicController {

    private TopicService topicService;

    @GetMapping("/get/course/{courseId}")
    public ResponseEntity<ApiResponseObject> getAllTopicsForSpecificCourse(@PathVariable String courseId){
        return topicService.getAllTopicsForSpecificCourse(courseId);
    }

    @PostMapping("/create/{courseId}")
    public ResponseEntity<ApiResponseObject> createTopic(@PathVariable String courseId, @RequestBody TopicDto topicDto){
        return topicService.createTopic(courseId,topicDto);
    }
}
