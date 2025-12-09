package com.skillforge.platform.models;

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
@Document(collection = "courses")
public class Course {
    @Id
    private String id;

    private String subjectId;
    private String instructorId;
    private String title;
    private String description;
    private SkillLevel difficultyLevel;
    private String thumbnailUrl;
    private List<String> learningObjectives;
    private List<String> prerequisites;
    private Integer estimatedDurationHours;

    @Builder.Default
    private Boolean published = false;

    @Builder.Default
    private Boolean isFeatured = false;

    @Builder.Default
    private Double price = 0.0;

    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
