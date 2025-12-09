package com.skillforge.platform.payloads;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InterviewAttemptResponseDto {
    private Double aiOverallScore; // 0-100
    private Map<String, Double> aiSkillEvaluation; // e.g., {"theory":0.85}
    private String aiFeedbackSummary;
    private InterviewAnswerDto[]interviewAnswerDtos;
}
