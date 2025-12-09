package com.skillforge.platform.repositories;

import com.skillforge.platform.models.InterviewAnswer;
import com.skillforge.platform.models.InterviewAttempt;
import com.skillforge.platform.models.InterviewQuestion;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface InterviewAttemptRepository extends MongoRepository<InterviewAttempt, String> {
    Optional<InterviewAttempt> findByInterviewId(String interviewId);
}
