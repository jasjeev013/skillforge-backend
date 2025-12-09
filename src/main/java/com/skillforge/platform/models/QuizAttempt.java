package com.skillforge.platform.models;

import com.skillforge.platform.constants.enums.AttemptStatus;
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
@Document(collection = "quiz_attempts")
public class QuizAttempt {
    @Id
    private String id;

    private String studentId;
    private String quizId;
    private LocalDateTime startedAt;
    private LocalDateTime submittedAt;
    private Integer timeSpentSeconds;
    private Double totalScore;
    private Double maxPossibleScore;
    private Double percentageScore;

    @Builder.Default
    private AttemptStatus status = AttemptStatus.IN_PROGRESS;

    private Map<String, Object> adaptiveDifficultyData;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}