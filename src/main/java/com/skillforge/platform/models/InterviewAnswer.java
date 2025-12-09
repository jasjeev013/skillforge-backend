package com.skillforge.platform.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "interview_answers")
public class InterviewAnswer {

    @Id
    private String id;

    private String interviewAttemptId;
    private String interviewQuestionId;

    private String studentAnswer;

    private String aiEvaluation; // AI-generated feedback
    private Double aiScore;      // Score for this question

    private Integer timeSpentSeconds;

    @CreatedDate
    private LocalDateTime createdAt;
}
