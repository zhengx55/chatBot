package com.cscc01.chatbot.backend.indexer;

public enum LuceneFieldConstants {
    CONTENT("contents"),
    FILE_NAME("filename"),
    FILE_PATH("filepath");

    private String text;

    LuceneFieldConstants(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
