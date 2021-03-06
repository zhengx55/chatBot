package com.cscc01.chatbot.backend.sql.repositories;

import java.util.List;

import com.cscc01.chatbot.backend.model.Feedback;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.lang.Nullable;

/**
 * This class defines CrudRepository specifically for user Feedback model
 * and these methods' signitures are auto-identified by Spring JPA in specific format
 */
public interface FeedbackRepository extends CrudRepository<Feedback, Long> {

    @Override
    @RestResource(exported = false)
    <S extends Feedback> S save(S entity);

    @Nullable
    List<Feedback> findAll();

}
