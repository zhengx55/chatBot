package com.cscc01.chatbot.backend.util;

import org.apache.log4j.BasicConfigurator;

import org.springframework.stereotype.Component;

@Component
public class LoggerInitializer {

    public LoggerInitializer (){
        BasicConfigurator.configure();
    }
}
