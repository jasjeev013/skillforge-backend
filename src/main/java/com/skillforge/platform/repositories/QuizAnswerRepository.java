package com.skillforge.platform.repositories;

import com.skillforge.platform.models.LearningMaterial;
import com.skillforge.platform.models.QuizAnswer;
import com.skillforge.platform.models.QuizAttempt;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface QuizAnswerRepository extends MongoRepository<QuizAnswer, String> {
    List<QuizAnswer> findAllByAttemptId(String attemptId);
}
