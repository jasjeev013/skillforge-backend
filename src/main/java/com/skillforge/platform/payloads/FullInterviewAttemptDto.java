package com.skillforge.platform.payloads;

import com.skillforge.platform.models.InterviewAnswer;
import com.skillforge.platform.models.InterviewAttempt;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FullInterviewAttemptDto {
    InterviewAttemptDto interviewAttemptDto;
    InterviewAnswerDto[] interviewAnswerDto;

}
