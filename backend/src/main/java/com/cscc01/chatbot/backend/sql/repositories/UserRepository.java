package com.cscc01.chatbot.backend.sql.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.web.bind.annotation.CrossOrigin;
import com.cscc01.chatbot.backend.model.User;

/**
 * This class defines CrudRepository specifically for User model
 * and these methods' signitures are auto-identified by Spring JPA in specific format
 */
@CrossOrigin
@RepositoryRestResource(collectionResourceRel = "users", path = "users")
public interface UserRepository extends CrudRepository<User, Long> {

    @Override
    @RestResource(exported = false)
    <S extends User> S save(S entity);

    @Override
    @RestResource(exported = false)
    void delete(User entity);

    @Override
    @RestResource(exported = false)
    void deleteAll();

    @Override
    @RestResource(exported = false)
    void deleteAll(Iterable<? extends User> entities);

    @Override
    @RestResource(exported = false)
    void deleteById(Long aLong);

    @Query("SELECT u FROM User u WHERE u.username = ?1")
    User findByUsername(@Param("username") String username);
}
