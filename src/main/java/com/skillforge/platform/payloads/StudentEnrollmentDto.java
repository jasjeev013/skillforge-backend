package com.skillforge.platform.payloads;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentEnrollmentDto {

    private String id;
    private String studentId;
    private String courseId;
    private LocalDateTime enrolledAt;
    private LocalDateTime completedAt;

    private Double currentProgressPercent;
    private Double overallScore;
    private String status;

    private LocalDateTime lastAccessedAt;
}
