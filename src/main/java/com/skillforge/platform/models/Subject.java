package com.skillforge.platform.models;

import com.skillforge.platform.constants.enums.SubjectCategory;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "subjects")
public class Subject {
    @Id
    private String id;

    private String name;
    private String description;
    private SubjectCategory category;
    private String thumbnailUrl;

    @Builder.Default
    private Boolean isActive = true;

    private String createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
