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
public class AIQuizQuestionDto {
    private QuestionType questionType;
    private String questionText;
    private Map<String, String> options; // For MCQ: { "A": "Option 1", "B": "Option 2" }
    private String correctAnswer; // A
    private String explanation; // One liner
    private Integer points; // Default: 1
    private Integer orderIndex;
}
