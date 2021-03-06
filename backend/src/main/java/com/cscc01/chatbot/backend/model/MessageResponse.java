package com.cscc01.chatbot.backend.model;

import java.util.UUID;

public class MessageResponse {

    private final UUID id;
    public String response;

    public MessageResponse() {
        id = UUID.randomUUID();
    }


    public void setResponse(String response) {
        this.response = response;
    }

    public String getResponse(){
        return this.response;
    }
}
