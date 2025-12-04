package com.skillforge.platform.controllers;

import com.skillforge.platform.payloads.ApiResponseObject;
import com.skillforge.platform.payloads.LearningMaterialDto;
import com.skillforge.platform.services.LearningMaterialService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v0/learning")
public class LearningMaterialController {

    private LearningMaterialService learningMaterialService;

    @PostMapping(value = "/create/{topicId}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<ApiResponseObject> createLearningMaterial(
            @PathVariable String topicId,
            @RequestPart("learningMaterialDto") LearningMaterialDto learningMaterialDto,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        return learningMaterialService.addLearningMaterial(topicId, learningMaterialDto, file);
    }


    @GetMapping("/topic/{topicId}")
    public ResponseEntity<ApiResponseObject> getAllLearningMaterialForSpecificTopic(@PathVariable String topicId){
        return learningMaterialService.getAllLearningMaterialForSpecificTopic(topicId);
    }
}
