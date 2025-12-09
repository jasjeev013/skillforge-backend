package com.skillforge.platform.repositories;

import com.skillforge.platform.models.QuizQuestion;
import com.skillforge.platform.models.StudentEnrollment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface QuizQuestionRepository extends MongoRepository<QuizQuestion, String> {
    List<QuizQuestion> findAllByQuizIdOrderByOrderIndex(String quizId);
}
