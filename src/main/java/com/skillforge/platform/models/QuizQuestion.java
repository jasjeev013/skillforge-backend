package com.skillforge.platform.models;

import com.skillforge.platform.constants.enums.QuestionType;
import com.skillforge.platform.constants.enums.SkillLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "quiz_questions")
public class QuizQuestion {
    @Id
    private String id;

    private String quizId;
    private QuestionType questionType;
    private String questionText;
    private Map<String, String> options; // For MCQ: { "A": "Option 1", "B": "Option 2" }
    private String correctAnswer;
    private String explanation;
    private SkillLevel difficultyLevel;

    @Builder.Default
    private Integer points = 1;

    private Integer orderIndex;

    @Builder.Default
    private Boolean isAiGenerated = false;

    private Map<String, Object> aiGenerationMetadata;
    private LocalDateTime createdAt;
}
