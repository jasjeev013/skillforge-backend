package com.skillforge.platform.models;

import com.skillforge.platform.constants.enums.PriorityLevel;
import com.skillforge.platform.constants.enums.RecommendationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "learning_recommendations")
public class LearningRecommendation {
    @Id
    private String id;

    private String studentId;
    private String materialId;
    private RecommendationType recommendationType;

    @Builder.Default
    private PriorityLevel priorityLevel = PriorityLevel.MEDIUM;

    private String reason;
    private Double aiConfidenceScore;

    @Builder.Default
    private Boolean isCompleted = false;

    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
}
