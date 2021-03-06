package com.cscc01.chatbot.backend.indexer.exception;

public class IndexAlreadyExistedException extends Exception {

    public IndexAlreadyExistedException(String filename) {
        super(filename + " is already Existed");
    }
}
