package com.skillforge.platform.repositories;

import com.skillforge.platform.models.StudentProgress;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface StudentProgressRepository  extends MongoRepository<StudentProgress, String> {
    List<StudentProgress> findAllByEnrollmentId(String enrollmentId);
    Integer countAllByEnrollmentIdAndCourseId(String enrollmentId,String courseId);
}
