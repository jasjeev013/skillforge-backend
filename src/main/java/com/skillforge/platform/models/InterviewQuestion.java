package com.skillforge.platform.models;

import com.skillforge.platform.constants.enums.SkillLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "interview_questions")
public class InterviewQuestion {

    @Id
    private String id;

    private String interviewId;
    private String questionText;
    private Integer orderIndex;

    @Builder.Default
    private SkillLevel difficultyLevel = SkillLevel.INTERMEDIATE;

    private String aiGuidancePrompt;

    @CreatedDate
    private LocalDateTime createdAt;
}
