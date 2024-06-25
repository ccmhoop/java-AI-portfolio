package com.conner.assistant.repository;

import com.conner.assistant.models.Embedding;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmbeddingRepository extends JpaRepository<Embedding, Long> {
}
