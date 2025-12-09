package com.skillforge.platform.payloads;

import com.skillforge.platform.models.QuizAnswer;
import com.skillforge.platform.models.QuizAttempt;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FullAttemptQuizDto {
    QuizAttemptDto quizAttemptDto;
    String[] questionIds;
    QuizAnswerDto[] quizAnswerDtos;
}
