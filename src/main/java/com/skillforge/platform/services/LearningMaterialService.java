package com.skillforge.platform.services;

import com.skillforge.platform.payloads.ApiResponseObject;
import com.skillforge.platform.payloads.LearningMaterialDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface LearningMaterialService {
    ResponseEntity<ApiResponseObject> addLearningMaterial(String topicId, LearningMaterialDto learningMaterialDto, MultipartFile file);
    ResponseEntity<ApiResponseObject> getAllLearningMaterialForSpecificTopic(String topicId);
}
