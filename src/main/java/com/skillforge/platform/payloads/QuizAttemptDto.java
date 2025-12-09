package com.skillforge.platform.payloads;

import com.skillforge.platform.constants.enums.AttemptStatus;
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
public class QuizAttemptDto {
    private String id;
    private String studentId;
    private String quizId;
    private LocalDateTime startedAt;
    private LocalDateTime submittedAt;
    private Integer timeSpentSeconds;
    private Double totalScore;
    private Double maxPossibleScore;
    private Double percentageScore;
    private AttemptStatus status;
    private Map<String, Object> adaptiveDifficultyData;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
