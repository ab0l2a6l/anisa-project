package org.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class DiscountExceptionHandler {

    @ExceptionHandler({NotFoundCoupon.class})
    public ResponseEntity<APIException> notFoundCoupon(NotFoundCoupon notFoundCoupon) {
        APIException apiException = new APIException(
          notFoundCoupon.getMessage(),
          HttpStatus.NOT_FOUND,
          LocalDateTime.now()
        );
        return new ResponseEntity<>(apiException, apiException.getHttpStatus());
    }
}
