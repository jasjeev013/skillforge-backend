package com.skillforge.platform.repositories;

import com.skillforge.platform.models.Course;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CourseRepository extends MongoRepository<Course, String> {

    List<Course> findAllByInstructorId(String instructorId);

}
