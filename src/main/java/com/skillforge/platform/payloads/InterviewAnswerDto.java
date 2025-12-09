package com.skillforge.platform.payloads;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InterviewAnswerDto {
    private String interviewQuestionId;
    private String studentAnswer;
    private String aiEvaluation; // AI-generated feedback
    private Double aiScore;      // Score for this question
    private Integer timeSpentSeconds;
}
