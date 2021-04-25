package io.batetolast1.githubstats.handler;

import io.batetolast1.githubstats.exception.ExceptionDetails;
import io.batetolast1.githubstats.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class GithubStatsExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionDetails> handleResourceNorFoundException(ResourceNotFoundException exception) {
        return new ResponseEntity<>(
                ExceptionDetails.builder()
                        .timestamp(LocalDateTime.now())
                        .status(NOT_FOUND.value())
                        .title("Resource not found.")
                        .details(exception.getMessage())
                        .developerMessage(exception.getClass().getName())
                        .build(),
                NOT_FOUND);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ExceptionDetails> handleConstraintViolationException(ConstraintViolationException exception) {
        return new ResponseEntity<>(
                ExceptionDetails.builder()
                        .timestamp(LocalDateTime.now())
                        .status(BAD_REQUEST.value())
                        .title("Constraint violation.")
                        .details(exception.getMessage())
                        .developerMessage(exception.getClass().getName())
                        .build(),
                BAD_REQUEST);
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<ExceptionDetails> handleHttpClientErrorException(HttpClientErrorException exception) {
        return new ResponseEntity<>(
                ExceptionDetails.builder()
                        .timestamp(LocalDateTime.now())
                        .status(HttpStatus.NOT_FOUND.value())
                        .title("Client error.")
                        .details(exception.getMessage())
                        .developerMessage(exception.getClass().getName())
                        .build(),
                NOT_FOUND);
    }

    @ExceptionHandler(HttpServerErrorException.class)
    public ResponseEntity<ExceptionDetails> handleHttpServerErrorException(HttpServerErrorException exception) {
        return new ResponseEntity<>(
                ExceptionDetails.builder()
                        .timestamp(LocalDateTime.now())
                        .status(INTERNAL_SERVER_ERROR.value())
                        .title("Internal server error.")
                        .details(exception.getMessage())
                        .developerMessage(exception.getClass().getName())
                        .build(),
                INTERNAL_SERVER_ERROR);
    }
}
