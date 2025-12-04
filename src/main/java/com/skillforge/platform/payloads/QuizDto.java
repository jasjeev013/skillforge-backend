package com.skillforge.platform.payloads;

import com.skillforge.platform.constants.enums.QuizType;
import com.skillforge.platform.constants.enums.SkillLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizDto {
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
    private Boolean isActive;
    private Boolean isAiGenerated;
    private String aiGenerationPrompt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
