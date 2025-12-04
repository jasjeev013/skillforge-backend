package com.skillforge.platform.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "instructor_profiles")
public class InstructorProfile {
    @Id
    private String id;

    @Indexed(unique = true)
    private String userId;

    private List<String> expertiseDomains;
    private String qualifications;
    private Integer yearsExperience;
    private Double hourlyRate;

    @Builder.Default
    private Boolean isVerified = false;
    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
