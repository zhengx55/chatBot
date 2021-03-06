package com.cscc01.chatbot.backend.sql;

import static org.junit.Assert.assertEquals;

import com.cscc01.chatbot.backend.model.Feedback;
import com.cscc01.chatbot.backend.sql.repositories.FeedbackRepository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;



@RunWith(SpringRunner.class)
@Transactional
@DataJpaTest
@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE)
public class FeedbackRepositoryTest {
     
    @Autowired
    private FeedbackRepository feedbackRepository;
     
    @Test
    public void givenUser_whenSave_thenGetOk() {
        Feedback feedback1 = new Feedback();
        feedback1.setMessage("test");
        feedbackRepository.save(feedback1);
         
        Feedback feedback2 = feedbackRepository.findAll().get(0);
        assertEquals("test", feedback2.getMessage());
    }

    @Test
    public void givenMultipleUser_whenSave_thenGetOk() {
        Feedback feedback1 = new Feedback();
        feedback1.setMessage("test1");
        feedbackRepository.save(feedback1);
        Feedback feedback2 = new Feedback();
        feedback2.setMessage("test2");
        feedbackRepository.save(feedback2);
        Feedback feedback3 = new Feedback();
        feedback3.setMessage("test3");
        feedbackRepository.save(feedback3);
         
        Feedback feedback11 = feedbackRepository.findAll().get(0);
        Feedback feedback21 = feedbackRepository.findAll().get(1);
        Feedback feedback31 = feedbackRepository.findAll().get(2);
        assertEquals("test1", feedback11.getMessage());
        assertEquals("test2", feedback21.getMessage());
        assertEquals("test3", feedback31.getMessage());
    }
}
