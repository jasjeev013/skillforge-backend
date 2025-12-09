package com.skillforge.platform.payloads;

import com.skillforge.platform.constants.enums.InterviewStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InterviewAttemptDto {

    private String id;
    private String studentId;
    private String interviewId;
    private LocalDateTime startedAt;
    private LocalDateTime submittedAt;
    private Double aiOverallScore; // 0-100
    private Map<String, Double> aiSkillEvaluation; // e.g., {"theory":0.85}
    private String aiFeedbackSummary;
    private InterviewStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
