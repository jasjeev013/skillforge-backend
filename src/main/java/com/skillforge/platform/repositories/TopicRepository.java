package com.skillforge.platform.repositories;

import com.skillforge.platform.models.InstructorProfile;
import com.skillforge.platform.models.Topic;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface TopicRepository extends MongoRepository<Topic, String> {

    Optional<Topic> findFirstByCourseIdOrderByOrderIndexDesc(String courseId);

    List<Topic> findAllByCourseIdOrderByOrderIndex(String courseId);
}
