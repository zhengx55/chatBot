package com.cscc01.chatbot.backend.sql;

import com.cscc01.chatbot.backend.model.User;
import com.cscc01.chatbot.backend.sql.repositories.UserRepository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Iterator;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@Transactional
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void givenUser_whenSave_thenGetOk() {
        User user1 = new User();
        user1.setUsername("test");
        userRepository.save(user1);
        User user2 = userRepository.findByUsername("test");
        assertEquals("test", user2.getUsername());
        userRepository.delete(user2);

    }


    @Test
    public void givenUser_whenSave_thenGetAllOk() {
        User user = new User();
        user.setUsername("test");
        userRepository.save(user);
        boolean containTestUser = false;
        int userCount = 0;

        for (User getUser : userRepository.findAll()) {
            if (getUser.getUsername().equals(user.getUsername())) {
                containTestUser = true;
            }
            userCount++;
        }

        assertNotEquals(0, userCount);
        assertTrue(containTestUser);
        userRepository.delete(user);
    }
}
