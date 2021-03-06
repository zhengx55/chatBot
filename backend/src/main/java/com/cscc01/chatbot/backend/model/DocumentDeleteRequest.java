package com.cscc01.chatbot.backend.model;

public class DocumentDeleteRequest {

    private String filename;
    //TODO: change to hold a USER entity after we create login system
    private String username;

    public String getFilename() {
        return filename;
    }

    public String getUsername() {
        return username;
    }
}
