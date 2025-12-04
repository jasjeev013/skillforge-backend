package com.skillforge.platform.models;

import com.skillforge.platform.constants.enums.LearningStyle;
import com.skillforge.platform.constants.enums.SkillLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "student_profiles")
public class StudentProfile {
    @Id
    private String id;

    @Indexed(unique = true)
    private String userId;

    @Builder.Default
    private SkillLevel currentLevel = SkillLevel.BEGINNER;

    @Builder.Default
    private Integer totalXp = 0;

    private String learningGoals;
    private LearningStyle preferredLearningStyle;
    private String timezone;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}