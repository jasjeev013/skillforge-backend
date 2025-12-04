package com.skillforge.platform.payloads;

import com.skillforge.platform.constants.enums.MaterialType;
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
public class LearningMaterialDto {
    private String id;
    private String topicId;
    private String title;
    private String description;
    private MaterialType contentType;
    private String contentUrl;
    private String contentText;
    private Integer durationMinutes;
    private SkillLevel difficultyLevel;
    private List<String> tags;
    private Integer orderIndex;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
