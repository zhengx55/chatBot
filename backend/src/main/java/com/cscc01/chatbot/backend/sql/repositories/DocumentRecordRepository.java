package com.cscc01.chatbot.backend.sql.repositories;

import com.cscc01.chatbot.backend.model.DocumentRecord;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

/**
 * This class defines CrudRepository specifically for DocumentRecord model
 * and these methods' signitures are auto-identified by Spring JPA in specific format
 */
@CrossOrigin
@RepositoryRestResource(collectionResourceRel = "documents", path = "documents")
public interface DocumentRecordRepository extends CrudRepository<DocumentRecord, Long> {

    @Override
    @RestResource(exported = false)
    <S extends DocumentRecord> S save(S entity);

    @Override
    @RestResource(exported = false)
    void delete(DocumentRecord entity);

    @Override
    @RestResource(exported = false)
    void deleteAll();

    @Override
    @RestResource(exported = false)
    void deleteAll(Iterable<? extends DocumentRecord> entities);

    @Query("SELECT d FROM DocumentRecord d WHERE d.name = ?1")
    DocumentRecord findByName(@Param("name") String name);

    @Nullable
    List<DocumentRecord> findAll();
}
