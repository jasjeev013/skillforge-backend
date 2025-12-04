package com.skillforge.platform.payloads;

import com.skillforge.platform.constants.enums.SkillLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopicDto {
    private String id;
    private String courseId;
    private String parentTopicId;
    private String title;
    private String description;
    private Integer orderIndex;
    private SkillLevel difficultyLevel;
    private Integer estimatedDurationMinutes;
    private List<String> learningObjectives;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}