package com.skillforge.platform.repositories;

import com.skillforge.platform.models.LearningMaterial;
import com.skillforge.platform.models.Topic;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface LearningMaterialRepository extends MongoRepository<LearningMaterial, String> {

    Optional<LearningMaterial> findFirstByTopicIdOrderByOrderIndexDesc(String topicId);
    List<LearningMaterial> findAllByTopicIdOrderByOrderIndex(String topicId);
}
