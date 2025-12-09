package com.skillforge.platform.repositories;

import com.skillforge.platform.models.LearningMaterial;
import com.skillforge.platform.models.Quiz;
import com.skillforge.platform.models.Topic;
import com.skillforge.platform.payloads.ApiResponseObject;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface QuizRepository extends MongoRepository<Quiz, String> {

    List<Quiz> findAllByTopicIdOrderByCreatedAt(String topicId);
    Integer countAllByTopicId(String  topicId);

}
