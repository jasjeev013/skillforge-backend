package com.skillforge.platform.payloads;

import com.skillforge.platform.models.QuizQuestion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FullQuizDto {
    QuizDto quizDto;
    QuizQuestionDto[] quizQuestions;
}
