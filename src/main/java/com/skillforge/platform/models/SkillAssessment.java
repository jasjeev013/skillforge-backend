package com.skillforge.platform.models;

import com.skillforge.platform.constants.enums.AssessmentMethod;
import com.skillforge.platform.constants.enums.SkillLevel;

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
@Document(collection = "skill_assessments")
@CompoundIndex(def = "{'studentId': 1, 'topicId': 1, 'assessedAt': -1}")
public class SkillAssessment {
    @Id
    private String id;

    private String studentId;
    private String topicId;
    private SkillLevel assessedSkillLevel;
    private Double confidenceScore; // 0.00 to 1.00
    private AssessmentMethod assessmentMethod;
    private LocalDateTime assessedAt;
    private String notes;
}

