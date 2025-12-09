package com.skillforge.platform.models;

import com.skillforge.platform.constants.enums.InterviewStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "interview_attempts")
public class InterviewAttempt {

    @Id
    private String id;

    private String studentId;
    private String interviewId;

    @CreatedDate
    private LocalDateTime submittedAt;

    private Double aiOverallScore; // 0-100
    private Map<String, Double> aiSkillEvaluation; // e.g., {"theory":0.85}
    private String aiFeedbackSummary;

    @Builder.Default
    private InterviewStatus status = InterviewStatus.IN_PROGRESS;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}