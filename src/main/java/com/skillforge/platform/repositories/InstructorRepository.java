package com.skillforge.platform.repositories;

import com.skillforge.platform.models.InstructorProfile;
import com.skillforge.platform.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface InstructorRepository extends MongoRepository<InstructorProfile, String> {
    InstructorProfile findByUserId(String userId);
}
