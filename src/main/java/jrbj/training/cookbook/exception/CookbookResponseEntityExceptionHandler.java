package jrbj.training.cookbook.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Global exception handling for the application.
 * Thrown exceptions are picked up here for handling. This allows simpler code in other classes, ie. controllers.
 *
 * Methods introduced to ensure structured logging of errors and controller responses.
 */
@Slf4j
@RequiredArgsConstructor
@ControllerAdvice
public class CookbookResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    private final ObjectMapper objectMapper;

    /**
     * Handles HttpClientExceptions as Bad Requests, returning details of errors in request
     */
    @ExceptionHandler(HttpClientException.class)
    public final ResponseEntity<String> handleHttpClientException(HttpClientException ex) {
        RestControllerProblem problem = RestControllerProblem.badRequest(ex.getProblemCode(), List.of(ex.getMessage()));
        log.warn(this.formatProblem(problem));
        return this.createResponseEntity(problem);
    }

    /**
     * Handles unspecified (often unexpected) exceptions as Internal Server Errors, not returning internal details.
     */
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<String> handleException(Exception ex) {
        RestControllerProblem problem = RestControllerProblem.internalServerError();
        log.error(this.formatProblem(problem), ex);
        return this.createResponseEntity(problem);
    }

    /**
     * Handles validation errors upon incoming requests as Bad Requests, returning details of errors in request
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        var errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        RestControllerProblem problem = RestControllerProblem.badRequest("invalid-arguments", errors);
        log.warn(this.formatProblem(problem));
        var response = createResponseEntity(problem);
        return new ResponseEntity<>(response.getBody(), response.getStatusCode());
    }

    /**
     * Formats encountered problem in a structured way for uniform structured logging of exceptions.
     */
    private String formatProblem(RestControllerProblem problem) {
        return String.format("Problem! Returning HTTP status [%d]. Id: [%s], Code: [%s], Detail: [%s]", problem.getStatus(), problem.getId(), problem.getCode(), problem.getDetails());
    }

    /**
     * Converts encountered problem into a ResponseEntity with desired status and body.
     */
    private ResponseEntity<String> createResponseEntity(RestControllerProblem problem) {
        try {
            return new ResponseEntity<>(this.objectMapper.writeValueAsString(problem), HttpStatus.valueOf(problem.getStatus()));
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }
    }
}
