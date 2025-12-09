package com.skillforge.platform.repositories;

import com.skillforge.platform.models.Interview;
import com.skillforge.platform.models.LearningMaterial;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface InterviewRepository extends MongoRepository<Interview, String> {
    Optional<Interview> findByEnrollmentId(String enrollmentId);
}
