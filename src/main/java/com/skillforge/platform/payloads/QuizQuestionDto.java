package com.skillforge.platform.payloads;

import com.skillforge.platform.constants.enums.QuestionType;
import com.skillforge.platform.constants.enums.SkillLevel;
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
public class QuizQuestionDto {
    private String id;
    private String quizId;
    private QuestionType questionType;
    private String questionText;
    private Map<String, String> options;
    private String correctAnswer;
    private String explanation;
    private SkillLevel difficultyLevel;
    private Integer points;
    private Integer orderIndex;
    private Boolean isAiGenerated;
    private Map<String, Object> aiGenerationMetadata;
    private LocalDateTime createdAt;
}
