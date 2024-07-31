package org.example.notification;

import org.springframework.stereotype.Component;

@Component
public class LoggerClientFallBack implements LoggerClient{
    @Override
    public LoggerDTO createLog(LoggerDTO logger) {
        return new LoggerDTO();
    }
}
