package org.example.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class APIException {
    private String message;
    private HttpStatus httpStatus;
    private LocalDateTime localDateTime;
}
