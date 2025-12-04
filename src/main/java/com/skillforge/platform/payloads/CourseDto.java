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
public class CourseDto {

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
    private Boolean isPublished;
    private Boolean isFeatured;
    private Double price;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
