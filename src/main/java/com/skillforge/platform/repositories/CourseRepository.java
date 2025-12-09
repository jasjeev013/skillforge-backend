package com.skillforge.platform.repositories;

import com.skillforge.platform.models.Course;
import org.springframework.data.mongodb.repository.MongoRepository;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

import java.util.List;

public interface CourseRepository extends MongoRepository<Course, String> {

    List<Course> findAllByInstructorId(String instructorId);
    List<Course> findAllByInstructorIdAndPublished(String instructorId,Boolean published);
    List<Course> findAllByPublished(Boolean published);

}
