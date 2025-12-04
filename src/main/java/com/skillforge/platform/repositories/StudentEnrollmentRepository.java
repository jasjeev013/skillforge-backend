package com.skillforge.platform.repositories;

import com.skillforge.platform.constants.enums.EnrollmentStatus;
import com.skillforge.platform.models.Course;
import com.skillforge.platform.models.StudentEnrollment;
import com.skillforge.platform.models.StudentProfile;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface StudentEnrollmentRepository extends MongoRepository<StudentEnrollment, String> {
    List<StudentEnrollment> findAllByStudentId(String studentId);
    List<StudentEnrollment> findAllByStudentIdAndStatus(String studentId, EnrollmentStatus status);
}
