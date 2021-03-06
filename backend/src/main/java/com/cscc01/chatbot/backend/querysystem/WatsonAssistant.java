package com.cscc01.chatbot.backend.querysystem;


import com.ibm.cloud.sdk.core.service.security.IamOptions;
import com.ibm.watson.assistant.v2.Assistant;
import com.ibm.watson.assistant.v2.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.LogManager;

@Service
public class WatsonAssistant {

    @Value("${assistant.apikey}")
    private String API_KEY;

    @Value("${assistant.version}")
    private String VERSION;

    @Value("${assistant.id}")
    private String ASSISTANT_ID;

    /**
     * create a IBM watson assistant instance
     *
     * @return
     */
    public Assistant createNewAssistant() {
        IamOptions options = new IamOptions.Builder()
                .apiKey(API_KEY)
                .build();
        return new Assistant(VERSION, options);

    }

    /**
     * create a IBM watson assistant session instance
     *
     * @param service
     * @return
     */
    public SessionResponse createSession(Assistant service) {
        CreateSessionOptions createSessionOptions = new CreateSessionOptions.Builder(ASSISTANT_ID).build();

        return service.createSession(createSessionOptions).execute().getResult();
    }

    /**
     * get message response from IBM watson assistant instance
     *
     * @param inputText
     * @param sessionResponse
     * @param assistant
     * @return
     */
    public MessageResponse getMessageResponse(String inputText, SessionResponse sessionResponse, Assistant assistant) {
        LogManager.getLogManager().reset();

        MessageInput input = new MessageInput.Builder().text(inputText).build();
        MessageOptions messageOptions = new MessageOptions.Builder(ASSISTANT_ID, sessionResponse.getSessionId())
                .input(input)
                .build();
        return assistant.message(messageOptions).execute().getResult();
    }
}

