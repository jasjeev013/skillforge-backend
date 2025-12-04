package com.skillforge.platform.services;

import com.skillforge.platform.payloads.ApiResponseObject;
import com.skillforge.platform.payloads.CourseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface CourseService {

    ResponseEntity<ApiResponseObject> createCourse(String instructorId, CourseDto courseDto);
    ResponseEntity<ApiResponseObject> addThumbnailPhoto(String courseId, MultipartFile multipartFile);
    ResponseEntity<ApiResponseObject> getAllCourses();
    ResponseEntity<ApiResponseObject> getSpecificCourse(String courseId);
    ResponseEntity<ApiResponseObject> getSpecificFullCourse(String courseId);
    ResponseEntity<ApiResponseObject> getAllCoursesForSpecificInstructor(String instructorId);
}
