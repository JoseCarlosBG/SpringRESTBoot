package com.epam.esm.controller.error;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PageNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePageNotFoundException(PageNotFoundException ex) {
        // You can obtain additional details from the exception or the request
        // For example, assuming you have a method in your exception like getRequestedPageNumber()
        Object details = ex.getRequestedPageNumber();
        String errorMessage = String.format("Requested resource not found (id = %s)", details);

        ErrorResponse errorResponse = new ErrorResponse(errorMessage, 40401);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}