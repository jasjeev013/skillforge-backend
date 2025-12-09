package com.skillforge.platform.repositories;

import com.skillforge.platform.models.QuizAttempt;
import com.skillforge.platform.models.QuizQuestion;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface QuizAttemptRepository extends MongoRepository<QuizAttempt, String> {
    Optional<QuizAttempt> findByQuizIdAndStudentId(String quizId,String studentId);
}
