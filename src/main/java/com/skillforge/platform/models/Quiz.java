package com.skillforge.platform.models;

import com.skillforge.platform.constants.enums.QuizType;
import com.skillforge.platform.constants.enums.SkillLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "quizzes")
public class Quiz {
    @Id
    private String id;

    private String topicId;
    private String instructorId;
    private String title;
    private String description;
    private QuizType quizType;
    private SkillLevel difficultyLevel;
    private Integer timeLimitMinutes;
    private Integer totalQuestions;
    private Double passingScore;

    @Builder.Default
    private Boolean isActive = true;

    @Builder.Default
    private Boolean isAiGenerated = false;

    private String aiGenerationPrompt;
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
