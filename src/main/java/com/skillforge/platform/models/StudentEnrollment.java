package com.skillforge.platform.models;

import com.skillforge.platform.constants.enums.EnrollmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "student_enrollments")
@CompoundIndex(def = "{'studentId': 1, 'courseId': 1}", unique = true)
public class StudentEnrollment {
    @Id
    private String id;

    private String studentId;
    private String courseId;

    @CreatedDate
    private LocalDateTime enrolledAt;
    private LocalDateTime completedAt;

    @Builder.Default
    private Double currentProgressPercent = 0.0;

    @Builder.Default
    private Double overallScore = 0.0;

    @Builder.Default
    private EnrollmentStatus status = EnrollmentStatus.ACTIVE;

    @LastModifiedDate
    private LocalDateTime lastAccessedAt;
}

