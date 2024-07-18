package org.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class ProductExceptionHandler {

    @ExceptionHandler({NotFoundProduct.class})
    public ResponseEntity<APIException> notFoundException(NotFoundProduct notFoundProduct) {
        APIException apiException = new APIException(notFoundProduct.getMessage()
                , HttpStatus.NOT_FOUND
                , LocalDateTime.now());
        return new ResponseEntity<>(apiException, apiException.getHttpStatus());
    }
}
