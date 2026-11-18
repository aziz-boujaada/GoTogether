package org.example.gotogether.adviceController;


import jakarta.servlet.http.HttpServletRequest;
import org.example.gotogether.dto.response.ApiResponseDto;
import org.example.gotogether.exception.InvalidValueException;
import org.example.gotogether.exception.ResourceAlreadyExistException;
import org.example.gotogether.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private ResponseEntity<ApiResponseDto> buildResponse(
            HttpServletRequest request,
            HttpStatus status,
            String message
    ) {
        ApiResponseDto apiErrorResponseDto = new ApiResponseDto(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getRequestURI()
        );
        return ResponseEntity.status(status).body(apiErrorResponseDto);
    }

    /// handle resource not found exception
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponseDto> handleResourceNotFound(
            ResourceNotFoundException exception,
            HttpServletRequest request
    ) {
        return buildResponse(request, HttpStatus.NOT_FOUND, exception.getMessage());
    }

    /// handle invalid value exception
    @ExceptionHandler(InvalidValueException.class)
    public ResponseEntity<ApiResponseDto> handleInvalidValue(
            InvalidValueException exception,
            HttpServletRequest request
    ) {
        return buildResponse(request, HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    /// handle the resource already exist exception
    @ExceptionHandler(ResourceAlreadyExistException.class)
    public ResponseEntity<ApiResponseDto> handleResourceAlreadyExist(
            ResourceAlreadyExistException exception,
            HttpServletRequest request
    ) {
        return buildResponse(request , HttpStatus.CONFLICT , exception.getMessage());
    }
}
