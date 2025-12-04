package com.skillforge.platform.controllers;

import com.skillforge.platform.payloads.ApiResponseObject;
import com.skillforge.platform.payloads.CourseDto;
import com.skillforge.platform.services.CourseService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v0/course")
public class CourseController {

    private CourseService courseService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponseObject> getAllCourses(){
        return courseService.getAllCourses();
    }

    @GetMapping("/all/{instructorId}")
    public ResponseEntity<ApiResponseObject> getAllCoursesForSpecifInstructor(@PathVariable String instructorId){
        return courseService.getAllCoursesForSpecificInstructor(instructorId);
    }

    @GetMapping("/get/{courseId}")
    public ResponseEntity<ApiResponseObject> getSpecificCourse(@PathVariable String courseId){
        return courseService.getSpecificCourse(courseId);
    }
    @GetMapping("/getFull/{courseId}")
    public ResponseEntity<ApiResponseObject> getSpecificFullCourse(@PathVariable String courseId){
        return courseService.getSpecificFullCourse(courseId);
    }

    @PostMapping("/create/{instructorId}")
    public ResponseEntity<ApiResponseObject> createCourse(@PathVariable String instructorId, @RequestBody CourseDto courseDto){
        return courseService.createCourse(instructorId,courseDto);
    }

    @PostMapping(value="/setThumbnail/{courseId}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<ApiResponseObject> setThumbnailURL(@PathVariable String courseId, @RequestPart(value = "file", required = false) MultipartFile file){
        return courseService.addThumbnailPhoto(courseId,file);
    }


}
