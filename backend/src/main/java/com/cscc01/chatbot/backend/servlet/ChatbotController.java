package com.cscc01.chatbot.backend.servlet;

import com.cscc01.chatbot.backend.model.Feedback;
import com.cscc01.chatbot.backend.model.UserFeedbackRequest;
import com.cscc01.chatbot.backend.model.UserQueryRequest;
import com.cscc01.chatbot.backend.querysystem.QuerySystemProcessor;
import com.cscc01.chatbot.backend.sql.repositories.FeedbackRepository;

import org.apache.lucene.queryparser.classic.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/")
public class ChatbotController {

    private final Logger LOGGER = LoggerFactory.getLogger(ChatbotController.class);

    @Inject
    private QuerySystemProcessor querySystemProcessor;

    @Inject
    private FeedbackRepository feedbackRepository;


    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public Map<String, Object> query(@RequestBody UserQueryRequest userQueryRequest) throws IOException, ParseException {
        return querySystemProcessor.getResponse(userQueryRequest.getMessage());
    }

    @RequestMapping(value = "/feedback", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> query(@RequestBody UserFeedbackRequest userFeedbackRequest) {
        Map<String, Object> response = new HashMap<>();
        String message = userFeedbackRequest.getMessage();
        Feedback feedback = new Feedback(message);
        LOGGER.info("Feedback: {}", feedback.getMessage());
        feedbackRepository.save(feedback);
        response.put("message", "Feedback successfully sent!");
        return ResponseEntity.ok().body(response);
    }
}
