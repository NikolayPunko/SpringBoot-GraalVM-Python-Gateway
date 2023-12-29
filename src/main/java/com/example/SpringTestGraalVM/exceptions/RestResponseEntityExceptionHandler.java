package com.example.SpringTestGraalVM.exceptions;

import com.example.SpringTestGraalVM.model.AppError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ AccessDeniedException.class })
    public ResponseEntity<AppError> handleAccessDeniedException(Exception ex, WebRequest request) {

        AppError response = new AppError("AccessDeniedException");
        return new ResponseEntity<AppError>(
                response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({ PersonNotFoundException.class })
    public ResponseEntity<AppError> handlePersonNotFoundException(Exception ex, WebRequest request) {

        AppError response = new AppError("PersonNotFoundException");
        return new ResponseEntity<AppError>(
                response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({ BadCredentialsException.class })
    public ResponseEntity<AppError> handleBadCredentialsException(Exception ex, WebRequest request) {

        AppError response = new AppError("BadCredentialsException");
        return new ResponseEntity<AppError>(
                response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({ PersonNotCreatedException.class })
    public ResponseEntity<AppError> handlePersonNotCreatedException(Exception ex, WebRequest request) {

        AppError response = new AppError("PersonNotCreatedException; " + ex.getMessage());
        return new ResponseEntity<AppError>(
                response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({ PricatNotFoundException.class })
    public ResponseEntity<AppError> handlePricatNotFoundException(Exception ex, WebRequest request) {

        AppError response = new AppError("PricatNotFoundException");
        return new ResponseEntity<AppError>(
                response, HttpStatus.NOT_FOUND);
    }


}
