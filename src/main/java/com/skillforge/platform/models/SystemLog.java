package com.skillforge.platform.models;

import com.skillforge.platform.constants.enums.LogAction;
import com.skillforge.platform.constants.enums.ResourceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "system_logs")
public class SystemLog {
    @Id
    private String id;

    private String userId;
    private LogAction action;
    private ResourceType resourceType;
    private String resourceId;
    private Map<String, Object> details;
    private String ipAddress;
    private String userAgent;
    private LocalDateTime createdAt;
}
