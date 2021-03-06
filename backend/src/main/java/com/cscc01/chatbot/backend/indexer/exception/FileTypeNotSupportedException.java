package com.cscc01.chatbot.backend.indexer.exception;

public class FileTypeNotSupportedException extends Exception {

    public FileTypeNotSupportedException(String fileType) {
        super(fileType + "file type is not supoorted");
    }
}
