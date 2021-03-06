package com.cscc01.chatbot.backend.model;


public class UrlUploadRequest {

    private String url;
    private String lastModifiedUser;

    UrlUploadRequest() {}

    UrlUploadRequest(String url, String lastModifiedUser) {
        this.url = url;
        this.lastModifiedUser = lastModifiedUser;
    }

    public String getUrl() {
        return url;
    }

    public String getLastModifiedUser(){
        return lastModifiedUser;
    }
}
