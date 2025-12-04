package com.skillforge.platform.payloads;

import com.skillforge.platform.constants.enums.LearningStyle;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentProfileDto {

    private String id;
    private String userId;

    private String currentLevel;
    private Integer totalXp;

    private String learningGoals;
    private LearningStyle preferredLearningStyle;

    private String timezone;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
