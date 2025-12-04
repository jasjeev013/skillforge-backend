package com.skillforge.platform.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "quiz_answers")
@CompoundIndex(def = "{'attemptId': 1, 'questionId': 1}", unique = true)
public class QuizAnswer {
    @Id
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