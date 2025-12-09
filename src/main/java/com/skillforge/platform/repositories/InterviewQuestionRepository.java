package com.skillforge.platform.repositories;

import com.skillforge.platform.models.InstructorProfile;
import com.skillforge.platform.models.InterviewQuestion;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface InterviewQuestionRepository extends MongoRepository<InterviewQuestion, String> {
    List<InterviewQuestion> findAllByInterviewId(String interviewId);
}
