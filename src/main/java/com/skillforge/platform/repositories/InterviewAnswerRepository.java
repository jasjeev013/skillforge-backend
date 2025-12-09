package com.skillforge.platform.repositories;

import com.skillforge.platform.models.InstructorProfile;
import com.skillforge.platform.models.InterviewAnswer;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface InterviewAnswerRepository extends MongoRepository<InterviewAnswer, String> {
    List<InterviewAnswer> findAllByInterviewAttemptId(String interviewAttemptId);
}
