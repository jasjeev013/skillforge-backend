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
public class QuizAnswerDto {
    private String id;
    private String attemptId;
    private String questionId;
    private String studentAnswer;
    private Boolean isCorrect;
    private Double pointsEarned;
    private String aiFeedback;
    private Integer timeSpentSeconds;
    private LocalDateTime createdAt;
}
