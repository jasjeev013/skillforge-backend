package com.skillforge.platform.models;

import com.skillforge.platform.constants.enums.EmotionalState;
import com.skillforge.platform.constants.enums.UnderstandingLevel;
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
@Document(collection = "student_progress")
@CompoundIndex(def = "{'enrollmentId': 1, 'learningMaterialId': 1}", unique = true)
public class StudentProgress {
    @Id
    private String id;

    private String enrollmentId;
    private String courseId;
    private String learningMaterialId;
    private String quizId;
    private LocalDateTime completedAt;

    @Builder.Default
    private Boolean completed = false;

    @Builder.Default
    private Integer timeSpentMinutes = 0;

    private UnderstandingLevel understandingLevel;
    private String notes;
    private EmotionalState emotionalFeedback;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
