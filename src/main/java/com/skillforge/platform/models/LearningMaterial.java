package com.skillforge.platform.models;

import com.skillforge.platform.constants.enums.MaterialType;
import com.skillforge.platform.constants.enums.SkillLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "learning_materials")
public class LearningMaterial {
    @Id
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

    @Builder.Default
    private Boolean isActive = true;

    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;
}