package org.example.notification;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class LoggerDTO {
    private String sender;
    private String message;
    private LocalDateTime localDateTime;
}
