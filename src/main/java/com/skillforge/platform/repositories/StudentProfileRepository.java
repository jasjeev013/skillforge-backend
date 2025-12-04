package com.skillforge.platform.repositories;

import com.skillforge.platform.models.Course;
import com.skillforge.platform.models.StudentProfile;
import org.springframework.data.mongodb.repository.MongoRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface StudentProfileRepository  extends MongoRepository<StudentProfile, String> {
    Optional<StudentProfile> findByUserId(String userId);
}
