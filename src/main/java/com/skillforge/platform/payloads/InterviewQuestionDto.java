package com.skillforge.platform.payloads;

import com.skillforge.platform.constants.enums.SkillLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InterviewQuestionDto {

    private String id;
    private String interviewId;
    private String questionText;
    private Integer orderIndex;
    private SkillLevel difficultyLevel;
    private String aiGuidancePrompt;
    private LocalDateTime createdAt;
}
