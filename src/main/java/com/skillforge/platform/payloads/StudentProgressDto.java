package com.skillforge.platform.payloads;

import com.skillforge.platform.constants.enums.EmotionalState;
import com.skillforge.platform.constants.enums.UnderstandingLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentProgressDto {

    private String id;
    private String enrollmentId;
    private String courseId;
    private String quizId;
    private String learningMaterialId;
    private LocalDateTime completedAt;

    private Boolean completed;
    private Integer timeSpentMinutes;

    private UnderstandingLevel understandingLevel; // UnderstandingLevel enum as String
    private String notes;
    private EmotionalState emotionalFeedback;  // EmotionalState enum as String

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
