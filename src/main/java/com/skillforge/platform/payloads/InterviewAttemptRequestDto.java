package com.skillforge.platform.payloads;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InterviewAttemptRequestDto {
    String[] questionIds;
    String[] questions;
    String[] answers;
}
