package edu.senacsp.health_management.advice;

import edu.senacsp.health_management.dto.response.general.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

/**
 *  Prevents unhandled error from leaking sensitive details (stack traces, etc.), to the client
 *  Intercepts {@link ResponseStatusException} and converts it into a clean {@link ResponseEntity}
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Business logic error
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handleResponseStatus(ResponseStatusException exception)
    {
        return ResponseEntity.status(exception.getStatusCode()).body(new ErrorResponse(exception.getReason()));
    }

    // Unhandled error
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception exception)
    {
        return ResponseEntity.status(500).body(new ErrorResponse("Internal server error"));
    }
}