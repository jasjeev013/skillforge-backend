package com.skillforge.platform.services.impl;

import com.skillforge.platform.constants.enums.MaterialType;
import com.skillforge.platform.models.LearningMaterial;
import com.skillforge.platform.models.Topic;
import com.skillforge.platform.payloads.ApiResponseObject;
import com.skillforge.platform.payloads.LearningMaterialDto;
import com.skillforge.platform.repositories.LearningMaterialRepository;
import com.skillforge.platform.repositories.TopicRepository;
import com.skillforge.platform.services.AWSS3Service;
import com.skillforge.platform.services.LearningMaterialService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class LearningMaterialServiceImpl implements LearningMaterialService {
    private TopicRepository topicRepository;
    private LearningMaterialRepository learningMaterialRepository;
    private ModelMapper modelMapper;
    private AWSS3Service awss3Service;
    @Override
    public ResponseEntity<ApiResponseObject> addLearningMaterial(String topicId, LearningMaterialDto learningMaterialDto,MultipartFile file) {
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new RuntimeException("Topic not found"));
        Optional<LearningMaterial> lastLearningMaterial = learningMaterialRepository.findFirstByTopicIdOrderByOrderIndexDesc(topicId);
        int order = 1;
        if (lastLearningMaterial.isPresent()) order = lastLearningMaterial.get().getOrderIndex()+1;
        String url = "";
        try {
           if (learningMaterialDto.getContentType().equals(MaterialType.VIDEO)){
               url = awss3Service.uploadVideo(file);
           } else if (learningMaterialDto.getContentType().equals(MaterialType.PDF)) {
               url = awss3Service.uploadPdf(file);
           }else {
               url = learningMaterialDto.getContentUrl();
           }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        LearningMaterial learningMaterial = LearningMaterial.builder()
                .title(learningMaterialDto.getTitle())
                .topicId(topic.getId())
                .description(learningMaterialDto.getDescription())
                .contentType(learningMaterialDto.getContentType())
                .contentText(learningMaterialDto.getContentText())
                .contentUrl(url)
                .durationMinutes(learningMaterialDto.getDurationMinutes())
                .difficultyLevel(learningMaterialDto.getDifficultyLevel())
                .tags(learningMaterialDto.getTags())
                .orderIndex(order)
                .build();

        LearningMaterial savedLM = learningMaterialRepository.save(learningMaterial);

        return new ResponseEntity<>(new ApiResponseObject("Learning Material Added Successfully", true,modelMapper.map(savedLM,LearningMaterialDto.class)), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<ApiResponseObject> getAllLearningMaterialForSpecificTopic(String topicId) {
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new RuntimeException("Topic not found"));

        List<LearningMaterial> learningMaterials = learningMaterialRepository.findAllByTopicIdOrderByOrderIndex(topic.getId());
        List<LearningMaterialDto> learningMaterialDtos = learningMaterials.stream()
                .map(learningMaterial -> modelMapper.map(learningMaterial,LearningMaterialDto.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(new ApiResponseObject("Fetched all learning material",true,learningMaterialDtos),HttpStatus.OK);
    }
}
